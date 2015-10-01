package com.example.administrator.myside.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class UserPreferences {
	private Context context=null;
	
	public UserPreferences(){}
	
	public UserPreferences(Context context)
	{
		this.context=context;
	}

	/**
	 * 保存用户名和记住用户名标志
	 * @param username
	 * @param ismark
	 */
	public void saveUserName(String username,boolean ismark)
	{
		SharedPreferences preferences=context.getSharedPreferences("username", Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putBoolean("ismark", ismark);
		editor.commit();
		if(ismark)
		{
			if(username!=null && !username.trim().equals(""))
			{
				editor.putString("username", username);
				editor.commit();
			}
		}else
		{
			editor.putString("username", "");
			editor.commit();
		}
		
	}

	/**
	 * 获取用户名和记住用户名标志
	 */
	public Map<String,Object> getPreferences()
	{
		Map<String,Object> map=new HashMap<String, Object>();
		SharedPreferences preferences=context.getSharedPreferences("username", Context.MODE_PRIVATE);
		map.put("username",preferences.getString("username", ""));
		map.put("ismark",preferences.getBoolean("ismark", false));
		return map;
	}
}
