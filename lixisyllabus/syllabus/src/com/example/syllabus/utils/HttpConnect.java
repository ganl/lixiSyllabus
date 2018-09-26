package com.example.syllabus.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * 
 * 
 * @author
 */
public class HttpConnect
{
    
    private final static String TAG = "HttpConnect";
    
    /**
     * Ĭ��ͼƬ��ȡ��ʱ
     */
    private final static int DEFAULT_BITMAP_TIMEOUT = 10 * 1000;
    
    /**
     * Ĭ��ͼƬ��ȡ��ʱ
     */
    private final static int DEFAULT_TIMEOUT = 15 * 1000;
    
    /**
     * http״̬����ֵ
     */
    private final static int HTTP_STATE_OK = 200;
    
    /**
     * Ĭ�ϱ���
     */
    public final static String ENCODING = "UTF-8";
    
    /**
     * �����С
     */
    private final static int BUFFER_SIZE = 1024 * 4;
    
    /**
     * http post��������httpclient
     * 
     * @param url �����url
     * @param list �����б�
     * @param headers httpͷ�б�
     * @param timeout ��ʱʱ��
     * @return �����ַ���
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String postHttpString(String url, List<NameValuePair> list, List<Header> headers, int timeout)
        throws Exception
    {
        
        // ��ӡ���ʵ�url
        
        // ��ӡ������ֵ��
        if (list != null)
        {
            for (NameValuePair pair : list)
            {
            }
        }
        
        // ��ӡͷ����
        if (headers != null)
        {
            for (Header head : headers)
            {
            }
        }
        
        HttpClient httpclient = new DefaultHttpClient();
        
        // ���ó�ʱʱ��,���ӳ�ʱ�Ͷ�ȡ��ʱ
        if (timeout > 0)
        {
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        }
        
        // ��������ͷ����,Ϊnull��ʱ������
        if (headers != null)
        {
            httpclient.getParams().setParameter("http.default-headers", headers);
        }
        HttpPost httppost = new HttpPost(url);
        
        // ���ñ����ʽ
        if (list != null)
        {
            httppost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
        }
        
        // ���������
        HttpResponse response = httpclient.execute(httppost);
        
        // ��ȡhttp״̬��
        int statuscode = response.getStatusLine().getStatusCode();
        if (statuscode == HttpStatus.SC_OK)
        {
            return EntityUtils.toString(response.getEntity());
        }
        else
        {
            // http״̬����ȷ,�����׳��쳣
            throw new Exception(statuscode + "");
        }
    }
    
    /**
     * http post��������httpclient,ʹ��Ĭ�ϵ�http����ͷ
     * 
     * @param url �����url
     * @param list �����б�
     * @param timeout ��ʱ�¼�
     * @return
     * @throws Exception
     * @author Administrator
     * @time 2011-12-26
     */
    public static String postHttpString(String url, List<NameValuePair> list, int timeout)
        throws Exception
    {
        return postHttpString(url, list, null, timeout);
    }
    
    /**
     * http post��������httpclient,ʹ��Ĭ�ϵ�http����ͷ,Ĭ�ϵĳ�ʱ�¼�
     * 
     * @param url �����url
     * @param list �����б�
     * @return
     * @throws Exception
     * @author Administrator
     * @time 2011-12-26
     */
    public static String postHttpString(String url, List<NameValuePair> list)
        throws Exception
    {
        return postHttpString(url, list, null, DEFAULT_TIMEOUT);
    }
    
    /**
     * http get��������httpclient����String
     * 
     * @param url ����url
     * @param httpͷ�б�
     * @param timeout ��ʱʱ��
     * @return �����ַ���
     * @throws Exception
     */
    public static String getHttpString(String url, List<Header> headers, int timeout)
        throws Exception
    {
        // ��ӡ���ʵ�url
        Log.d("test", url);
        // ��ӡ������ֵ��
        HttpClient httpclient = new DefaultHttpClient();
        // ���ó�ʱʱ��,���ӳ�ʱ�Ͷ�ȡ��ʱ
        if (timeout > 0)
        {
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        }
        // ��������ͷ����,Ϊnull��ʱ������
        if (headers != null)
        {
            httpclient.getParams().setParameter("http.default-headers", headers);
        }
        HttpGet httpget = new HttpGet(url);
        // ���������
        HttpResponse response = httpclient.execute(httpget);
        // ��ȡhttp״̬��
        int statuscode = response.getStatusLine().getStatusCode();
        if (statuscode == HttpStatus.SC_OK)
        {
            String s = EntityUtils.toString(response.getEntity());
            Log.d("test", s);
            return s;
        }
        else
        {
            // http״̬����ȷ,�����׳��쳣
            throw new Exception(statuscode + "");
        }
    }
    
    /**
     * http get��������httpclient����String��ʹ��Ĭ�ϵ�����ͷ
     * 
     * @param url ����url
     * @param timeout ��ʱʱ��
     * @return �����ַ���
     * @throws Exception
     */
    public static String getHttpString(String url, int timeout)
        throws Exception
    {
        return getHttpString(url, null, timeout);
    }
    
    /**
     * http get��������httpclient����String��ʹ��Ĭ�ϵ�����ͷ��
     * 
     * @param url ����url
     * @param timeout ��ʱʱ��
     * @return �����ַ���
     * @throws Exception
     */
    public static String getHttpString(String url)
        throws Exception
    {
        return getHttpString(url, null, DEFAULT_TIMEOUT);
    }
    
    /**
     * POST����
     * 
     * @param url
     * @param parmas
     * @return
     */
    public static String postHttpStringQ(String url, Map<String, String> parmas)
    {
        String s = null;
        DefaultHttpClient client = new DefaultHttpClient();// http�ͻ���
        // ���ó�ʱʱ��,���ӳ�ʱ�Ͷ�ȡ��ʱ
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        HttpPost httpPost = new HttpPost(url);
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        
        if (parmas != null)
        {
            Set<String> keys = parmas.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();)
            {
                String key = (String)i.next();
                pairs.add(new BasicNameValuePair(key, parmas.get(key)));
            }
        }
        try
        {
            
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
            
            /*
             * 
             * ��POST���ݷ���HTTP����
             */
            httpPost.setEntity(p_entity);
            
            /*
             * 
             * ����ʵ�ʵ�HTTP POST����
             */
            HttpResponse response = client.execute(httpPost);
            
            // HttpEntity entity = response.getEntity();
            // ��ȡhttp״̬��
            int statuscode = response.getStatusLine().getStatusCode();
            if (statuscode == HttpStatus.SC_OK)
            {
                s = EntityUtils.toString(response.getEntity());
                
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        Log.d("test", s);
        return s;
    }
    
    public static boolean isNetworkHolding(Context context)
    {
        ConnectivityManager con = (ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
        return (wifi || internet);
    }
}
