package com.park61.moduel.login.bean;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.tool.CommonMethod;

import org.json.JSONObject;

import java.io.File;

/**
 * @Description:账号（用户管理）
 * @author:Cai
 * @time:2013-11-26 下午4:01:01
 */
public class UserManager {
	public static final String fileName = "userBean";
	private UserBean userInfo = null;
	private static UserManager userManager;

	public UserManager() {
	}

	public static UserManager getInstance() {
		if (userManager == null) {
			userManager = new UserManager();
		}
		return userManager;
	}

	/**
	 * 设置（保存）userInfo（账号相关信息）
	 */
	public void setAccountInfo(UserBean userInfo, Context mContext) {
		this.userInfo = userInfo;
		CommonMethod.saveObject(mContext, userInfo, fileName);
	}

	/**
	 * 重设(保存)账号信息
	 */
	public void reSetAccountInfo(Context mContext) {
		CommonMethod.saveObject(mContext, userInfo, fileName);
	}

	/**
	 * 获取accountInfo（账号相关信息）
	 */
	public UserBean getAccountInfo(Context mContext) {
		if (userInfo == null) {
			userInfo = (UserBean) CommonMethod.readObject(mContext, fileName);
		}
		return userInfo;
	}

	/**
	 * 删除accountInfo（账号相关信息），切换账号、退出程序需要用到
	 */
	public void deleteAccountInfo(Context mContext) {
		userInfo = null;
		File data = mContext.getFileStreamPath(fileName);
		data.delete();
	}

	/**
	 * 根据平台返回json解析用户相关信息，并保存
	 */
	public void parserAccountInfo(JSONObject jsonData,Context mContext) {
		UserBean userInfo = new UserBean();
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
		userInfo = gson.fromJson(jsonData.toString(), UserBean.class);
		setAccountInfo(userInfo,mContext);
	}

}
