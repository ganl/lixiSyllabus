/*
 * ������վ:http://www.ShareSDK.cn
 * ����֧��QQ: 4006852216
 * �ٷ�΢��:ShareSDK   ����������°汾�Ļ������ǽ����һʱ��ͨ��΢�Ž��汾�����������͸��������ʹ�ù��������κ����⣬Ҳ����ͨ��΢��������ȡ����ϵ�����ǽ�����24Сʱ�ڸ���ظ���
 *
 * Copyright (c) 2013�� ShareSDK.cn. All rights reserved.
 */

package cn.sharesdk.onekeyshare;

import com.austgl.syllabus.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;
import cn.sharesdk.framework.Platform;

/**
 * ShareCore�ǿ�ݷ����ʵ�ʳ��ڣ�����ʹ���˷���ķ�ʽ����ϴ��ݽ�����HashMap��
 *����{@link ShareParams}���󣬲�ִ�з���ʹ��ݷ�������Ҫ����Ŀ��ƽ̨
 */
public class ShareCore {
	private ShareContentCustomizeCallback customizeCallback;

	/** �������ڷ�������У����ݲ�ͬƽ̨�Զ���������ݵĻص� */
	public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
		customizeCallback = callback;
	}

	/**
	 * ��ָ��ƽ̨��������
	 * <p>
	 * <b>ע�⣺</b><br>
	 * ����data�ļ�ֵ��Ҫ�ϸ���{@link ShareParams}��ͬ��������ֶ���������
	 *�����޷�������ֶΣ�Ҳ�޷�������ֵ��
	 */
	public boolean share(Platform plat, HashMap<String, Object> data) {
		if (plat == null || data == null) {
			return false;
		}

		Platform.ShareParams sp = null;
		try {
			sp = getShareParams(plat, data);
		} catch(Throwable t) {
			sp = null;
		}

		if (sp != null) {
			if (customizeCallback != null) {
				customizeCallback.onShare(plat, sp);
			}
			plat.share(sp);
		}
		return true;
	}

	private Platform.ShareParams getShareParams(Platform plat,
			HashMap<String, Object> data) throws Throwable {
		String className = plat.getClass().getName() + "$ShareParams";
		Class<?> cls = Class.forName(className);
		if (cls == null) {
			return null;
		}

		Object sp = cls.newInstance();
		if (sp == null) {
			return null;
		}

		for (Entry<String, Object> ent : data.entrySet()) {
			try {
				Field fld = cls.getField(ent.getKey());
				if (fld != null) {
					fld.setAccessible(true);
					fld.set(sp, ent.getValue());
				}
			} catch(Throwable t) {}
		}

		return (Platform.ShareParams) sp;
	}

	/** �ж�ָ��ƽ̨�Ƿ�ʹ�ÿͻ��˷��� */
	public static boolean isUseClientToShare(String platform) {
		if ("Wechat".equals(platform) || "WechatMoments".equals(platform)
				|| "ShortMessage".equals(platform) || "Email".equals(platform)
				|| "GooglePlus".equals(platform) || "QQ".equals(platform)
				|| "Pinterest".equals(platform)) {
			return true;
		}

		return false;
	}

}
