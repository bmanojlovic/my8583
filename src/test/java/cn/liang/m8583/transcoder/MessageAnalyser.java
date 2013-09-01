package cn.liang.m8583.transcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.message.support.BalanceRequest;
import cn.liang.m8583.message.support.WorkingKeyResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.CipherException;
import cn.liang.m8583.transcoder.exception.DecodeException;

import com.sfpay.framework.common.json.JSONUtils;

/**
 * 报文分析器
 * 16进制字符串写成一行添加在message.txt下。
 * @author 325336, Liang Yabao
 * 2012-4-13
 */
public class MessageAnalyser {

	private static final KeyFinder kf = new KeyFinderDemo();
	private static final DesEncryptor encryptor = new DesEncryptor();
	private static final DefaultTranscoder transcoder = new DefaultTranscoder(kf, encryptor);
	

	
	public static void main(String[] args) throws IOException, DecodeException, CipherException {
		InputStream is = MessageAnalyser.class
				.getResourceAsStream("message.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if(line.startsWith("#")){
				continue;
			}
			if (!line.isEmpty()) {
				byte[] data = MessageUtil.hex_2_byte(line);
				Message mes = transcoder.decode(data);
				System.out.println(mes.getClass());
				System.out.println(JSONUtils.fromObject(mes));
				
				if(mes instanceof BalanceRequest){
					BalanceRequest bal = (BalanceRequest) mes;
					Encryptor enc = transcoder.getEncryptor();
					System.out.println(MessageUtil.byte2hex(bal.getPin()));
					String pwd = enc.decryptPassword(kf.findKey(bal.getTerminalId()), bal.getPin());
					System.out.println("余额查询，密码："+pwd);
				}
				
				if(mes instanceof WorkingKeyResponse){
					WorkingKeyResponse wkr = (WorkingKeyResponse) mes;
					System.out.println("getNewWorkingKey:"+new String(MessageUtil.byte2hex(wkr.getNewWorkingKey())));
					
					encryptor.decryptWorkingKey(kf.findMasterKey(), wkr.getNewWorkingKey());
				}
			}
		}
	}

}
