package com.china.net;

import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.androidquery.util.AQUtility;
import com.androidquery.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public abstract class BaseProtocol<T> implements Transformer, Constants {
    public static int OPTION_NONE = 0;
    public static int OPTION_GET_CHILD_OBJECT = 1;
    public static int OPTION_GET_FIRST_OBJECT = 2;
    private static GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");

    // 协议的目标对象类型和转换参数
    private Class<T> _transformType;
    private int _transformOption;

    // 协议的用户可以设置的请求参数
    private int _method;
    private String _url;
    private Map<String, String> _headers;
    private Map<String, Object> _params;
    private HttpEntity _postEntity;

    // 执行HTTP请求时传递给AQuery的参数（根据用户设置设置的参数而生成）
    private boolean _httpPrepared;
    private String _httpNetworkUrl;
    private String _httpCacheUrl;
    private Map<String, String> _httpHeaders;
    private Map<String, Object> _httpParams;
    private AjaxCallback<T> _httpCallback;


    public BaseProtocol(Class<T> type, int... option) {
        _method = Constants.METHOD_DETECT;
        _transformType = type;
        _transformOption = option.length > 0 ? option[0] : OPTION_NONE;
        _httpPrepared = false;
    }

    // *********************************************************************************************************
    // 以下是对外函数。
    // *********************************************************************************************************

    /**
     * 设置协议请求的HTTP方法
     *
     * @param method HTTP方法
     * @return self
     */
    public BaseProtocol<T> method(int method) {
        _method = method;
        _httpPrepared = false;
        return this;
    }

    public int getMethod(){
        return _method;
    }

    /**
     * 设置协议请求的URL地址
     *
     * @param url URL地址
     * @return self
     */
    public BaseProtocol<T> url(String url) {
        _url = url;
        _httpPrepared = false;
        return this;
    }

    /**
     * 设置协议请求的HTTP头
     *
     * @param headerPairs 以name/value配对出现的HTTP头参数
     * @return self
     */
    public BaseProtocol<T> headers(String... headerPairs) {
        if (headerPairs.length < 2) return this;
        if (_headers == null) _headers = new HashMap<String, String>();
        for (int i = 0; i < headerPairs.length - 1; i += 2) {
            _headers.put(headerPairs[i], headerPairs[i + 1]);
        }
        _httpPrepared = false;
        return this;
    }

    /**
     * 设置协议请求的HTTP参数
     *
     * @param paramPairs 以name/value配对出现的HTTP参数
     * @return self
     */
    public BaseProtocol<T> params(Object... paramPairs) {
        if (paramPairs.length < 2) return this;
        if (_params == null) _params = new HashMap<String, Object>();
        for (int i = 0; i < paramPairs.length - 1; i += 2) {
            _params.put(paramPairs[i].toString(), paramPairs[i + 1]);
        }
        _httpPrepared = false;
        return this;
    }

    /**
     * 设置协议请求的HTTP POST内容。
     * 如果设置了非空的POST内容，则HTTP请求类型自动改为POST方法，且请求
     * 参数中的params参数将添加到请求的url中，而不再放入POST内容中。
     *
     * @param postEntity HTTP POST的HttpEntity对象。
     * @return self
     */
    public BaseProtocol<T> postEntity(HttpEntity postEntity) {
        _postEntity = postEntity;
        if (postEntity != null){
        	if(_method == Constants.METHOD_DETECT)
        		_method = Constants.METHOD_POST;
        }
        _httpPrepared = false;
        return this;
    }

    public void requestInvalidate() {
        _httpPrepared = false;
    }

    public BaseProtocol<T> postJson(Object jsonBean) {
        return postString(new Gson().toJson(jsonBean), "UTF-8", "application/json");
    }

    public BaseProtocol<T> postJsonMap(Object ... kvPairs){
        if (kvPairs.length < 2) return this;
        /*StringBuilder sb = new StringBuilder("{");

        for (int i = 0; i < kvPairs.length - 1; i += 2) {
            if(i>=2)
                sb.append(",");
            sb.append('"').append(kvPairs[i]).append("\":").append(kvPairs[i + 1] != null ? kvPairs[i + 1] : "");
        }
        sb.append("}");*/

        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < kvPairs.length - 1; i += 2) {
            try {
                jsonObject.put(kvPairs[i].toString(), kvPairs[i + 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return postString(jsonObject.toString(), "UTF-8", "application/json");
    }

    public BaseProtocol<T> postJsonMap(Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();

        Set<Map.Entry<String, Object>> set = map.entrySet();
        for(Map.Entry<String, Object> entry : set){
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return postString(jsonObject.toString(), "UTF-8", "application/json");
    }
    /**
     * 设置协议请求的HTTP POST字符串的内容。
     *
     * @param postData    HTTP POST的字符串
     * @param charset     字符串的编码格式
     * @param contentType （可选）HTTP POST请求头中ContentType的定义。缺省为：ContentType.TEXT_PLAIN（“text/plain”）
     * @return
     */
    public BaseProtocol<T> postString(String postData, String charset, String... contentType) {
        try {
            StringEntity postEntity = new StringEntity(postData, charset);
            if (contentType.length > 0 && contentType[0] != null && contentType[0].length() > 0) {
                postEntity.setContentType(contentType[0]);
            }
            return postEntity(postEntity);
        } catch (UnsupportedEncodingException e) {
            return this;
        }
    }

    public BaseProtocol<T> postStringGzip(String postData, String charset,
                                          String... contentType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream out;
        try {
            out = new GZIPOutputStream(baos);
            out.write(postData.getBytes(charset));
            out.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] zipArr = baos.toByteArray();
        InputStreamEntity postEntity = new InputStreamEntity(
                new ByteArrayInputStream(zipArr), zipArr.length);
        if (contentType.length > 0 && contentType[0] != null
                && contentType[0].length() > 0) {
            postEntity.setContentType(contentType[0]);
        }
        return postEntity(postEntity);
    }

    /**
     * 设定协议执行的callback回调对象。
     *
     * @param callback 回调对象
     */
    public BaseProtocol<T> callback(AjaxCallback<T> callback) {
        _httpCallback = callback;
        return this;
    }

    /**
     * 设定协议执行的callback回调函数。
     *
     * @param handler  回调函数所在的对象。此对象为弱引用，如果对象被销毁，则不会调用回调函数。
     * @param callback 回调函数名称
     */
    public BaseProtocol<T> callback(Object handler, String callback) {
        _httpCallback = new AjaxCallback<T>().weakHandler(handler, callback);
        return this;
    }

    /**
     * 返回协议执行的callback回调对象
     *
     * @return null - 协议尚未被执行；否则返回对应的回调对象
     */
    public AjaxCallback<T> getCallback() {
        return _httpCallback;
    }

    /**
     * 返回协议请求网络的url。
     *
     * @return
     */
    public String getNetworkUrl() {
        prepareExecute();
        return _httpNetworkUrl;
    }

    /**
     * 返回协议请求缓存的url。
     *
     * @return
     */
    public String getCacheUrl() {
        prepareExecute();
        return _httpCacheUrl;
    }

    /**
     * 返回协议请求缓存的结果对象文件。
     *
     * @param includeNonExist （可选）如果为false（缺省），则当缓存文件不存在时，返回null；否则，返回文件对象
     * @return null - 缓存不存在
     */
    public File getCacheFile(boolean... includeNonExist) {
        String cacheUrl = getCacheUrl();
        File file = AQUtility.getExistedCacheByUrl(AQUtility.getCacheDir(AQUtility.getContext(), AQuery.CACHE_DEFAULT), cacheUrl);
        if (file == null) {
            file = AQUtility.getExistedCacheByUrl(AQUtility.getCacheDir(AQUtility.getContext(), AQuery.CACHE_PERSISTENT), cacheUrl);
            if (file == null && includeNonExist.length > 0 && includeNonExist[0] == true) {
                file = AQUtility.getCacheFile(AQUtility.getCacheDir(AQUtility.getContext(), AQuery.CACHE_DEFAULT), cacheUrl);
            }
        }
        return file;
    }

    /**
     * 返回协议请求缓存的结果对象。
     *
     * @param expire   > 0, 缓存失效时间（毫秒）; = 0, 缓存一直有效; < 0, 缓存一直失效;
     * @param encoding （可选）数据的字符编码，缺省为UTF-8
     * @return null - 缓存不存在/缓存失效/转换失败
     */
    public T getCacheResult(long expire, String... encoding) {
        if (expire < 0) return null;
        File file = getCacheFile();
        if (file == null) return null;
        if (expire != 0 && System.currentTimeMillis() - file.lastModified() > expire) return null;
        try {
            String enc = encoding.length > 0 ? encoding[0] : "UTF-8";
            byte[] cacheByte = AQUtility.toBytes(new FileInputStream(file));
            return transform(getCacheUrl(), _transformType, enc, cacheByte, new AjaxStatus());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 修改协议请求缓存的结果对象。
     *
     * @param value    修改的结果对象
     * @param encoding （可选）数据的字符编码，缺省为UTF-8
     * @return 成功返回True；否则返回False
     */
    public boolean setCacheResult(T value, String... encoding) {
        try {
            File file = getCacheFile(true);
            if (value == null || file == null) return false;
            long lastModified = file.exists() ? file.lastModified() : System.currentTimeMillis();
            String enc = encoding.length > 0 ? encoding[0] : "UTF-8";
            byte[] json = (_transformType.getName() + gsonBuilder.create().toJson(value)).getBytes(enc);
            AQUtility.write(file, json);
            file.setLastModified(lastModified);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * 返回协议请求的结果对象是否缓存
     *
     * @return true - 缓存存在；false - 缓存不存在
     */
    public boolean isCacheExist() {
        return !isCacheExpired(0);
    }

    /**
     * 返回协议请求缓存的结果对象是否失效。
     *
     * @param expire > 0, 缓存失效时间（毫秒）; = 0, 缓存一直有效; < 0, 缓存一直过期;
     * @return true - 缓存不存在或缓存过期；false - 缓存存在且未过期
     */
    public boolean isCacheExpired(long expire) {
        if (expire < 0) return true;
        File file = getCacheFile();
        if (file == null) return true;
        if (expire != 0 && System.currentTimeMillis() - file.lastModified() > expire) return true;
        return false;
    }

    /**
     * 设置缓存对象的时间使之为失效。
     *
     * @param expire > 0, 缓存对象时间提前当前时间的时间差（毫秒）; <= 0, 删除缓存文件;
     */
    public void setCacheExpired(long expire) {
        File file = getCacheFile();
        if (file == null) return;
        if (expire <= 0) {
            file.delete();
        } else if (System.currentTimeMillis() - file.lastModified() < expire) {
            file.setLastModified(System.currentTimeMillis() - expire);
        }
    }

    /**
     * 取消协议的请求。请求被取消后，在状态变成完成前不能继续请求。如果请求未启动或已完成，则不做任何操作。
     */
    public void cancelRequest() {
        if (getFinished()) return;
        _httpCallback.abort();
    }

    /**
     * 返回协议请求的结果对象。
     *
     * @return null - 协议请求失败或尚未完成。
     */
    public T getResult() {
        if (_httpCallback == null) return null;
        return _httpCallback.getResult();
    }

    /**
     * 返回协议请求的结果状态对象。
     *
     * @return null - 尚未发起协议请求。
     */
    public AjaxStatus getStatus() {
        if (_httpCallback == null) return null;
        return _httpCallback.getStatus();
    }

    /**
     * 返回协议请求的是否完成。
     *
     * @return true - 协议请求已完成或尚未发起；false - 协议请求正在执行。
     */
    public boolean getFinished() {
        if (_httpCallback == null || _httpCallback.getComplete()) return true;
        return false;
    }

    /**
     * 返回协议请求的是否被取消。
     *
     * @return true - 协议请求被取消，尚未结束；false - 协议未被取消或已经结束。
     */
    public boolean getCanceled() {
        if (_httpCallback != null && _httpCallback.getAbort()) return true;
        return false;
    }

    /**
     * 执行协议对应的HTTP请求。如果之前的请求尚未完成，则返回false。用户需要重新设置callback，或者等待请求完成。
     * 如果不包含expire参数，如果callback对象没有设置相关参数的话，返回的结果将不会缓存。
     * 如果包含expire参数，则先检查是否有缓存，再发起HTTP请求，返回的结果将缓存。
     *
     * @param executor
     * @param expire   （可选）缓存过期时间。> 0, 缓存过期时间（毫秒）; = 0, 缓存一直有效; < 0, 缓存一直过期。
     * @return true - 请求被执行；false - 之前的请求尚未完成，需要重新设置callback，或者等待请求完成。
     */
    public boolean execute(AQuery executor, long... expire) {
        if (_httpCallback == null) _httpCallback = new AjaxCallback<T>();
        if (_httpCallback.getStatus() != null && !_httpCallback.getStatus().getDone()){
            Log.e("BaseProtocol", "execute fail!!! " + _httpCallback.getStatus());
            return false;
        }
        if (expire.length > 0) _httpCallback.fileCache(true).expire(expire[0]);
        doExecute(executor, _httpCallback);
        return true;
    }

    // *********************************************************************************************************
    // 以下是内部函数，派生类可重载这些函数以实现不同的实现和逻辑
    // *********************************************************************************************************

    /**
     * 设置callback对象的HTTP相关参数，准备执行HTTP请求。
     */
    protected void prepareExecute() {
        if (_httpPrepared) return;
        // setup flag
        _httpPrepared = true;
        // setup http header
        _httpHeaders = getRequestHeaders();
        // setup http url & params
        String url = getRequestUrl();
        Map<String, Object> params = getRequestParams();
        if (_method == Constants.METHOD_GET || _method == Constants.METHOD_DELETE
                || (_method == Constants.METHOD_DETECT && params == null && _postEntity == null)) {
            // HTTP GET和DELETE请求
            _httpParams = null;
            _httpNetworkUrl = getHttpGetUrl(url, params);
            _httpCacheUrl = getHttpCacheUrl(url, params);
        } else if (_postEntity == null) {
            // HTTP POST和PUT请求，请求params通过POST内容以form urlEncoded的格式提交。
            _httpParams = params;
            _httpNetworkUrl = url;
            _httpCacheUrl = getHttpCacheUrl(url, params);
        } else {
            // HTTP POST和PUT请求，请求params通过url提交，postEntity通过POST内容提交。
            _httpParams = new HashMap<String, Object>();
            _httpParams.put(AQuery.POST_ENTITY, _postEntity);
            Header contentType = _postEntity.getContentType();
            if (contentType != null) {
                if (_httpHeaders == null) _httpHeaders = new HashMap<String, String>();
                _httpHeaders.put(contentType.getName(), contentType.getValue());
            }
            _httpNetworkUrl = getHttpGetUrl(url, params);
            _httpCacheUrl = getHttpCacheUrl(url, params);
        }
    }

    /**
     * 设置callback对象的HTTP相关参数，准备执行HTTP请求，包括：method/url/networkUrl/header/params/type/transform。
     *
     * @param executor
     * @param callback
     */
    protected void doExecute(AQuery executor, AjaxCallback<T> callback) {
        // setup request parameters
        prepareExecute();
        // setup callback http header
        if (_httpHeaders != null) {
            for (Map.Entry<String, String> entry : _httpHeaders.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && key.length() != 0) callback.header(key, value != null ? value : "");
            }
        }
        // set callback attributes
        callback.method(_method).networkUrl(_httpNetworkUrl).url(_httpCacheUrl).params(_httpParams).type(_transformType).transformer(this);
        // execute request
        executor.ajax(callback);
    }

    /**
     * 返回协议请求的RootUrl，任何HTTP请求的地址都会加上此RootUrl。派生类可以重载该函数。
     *
     * @return RootUrl, 如果未指定则返回null
     */
    protected String getRootUrl() {
        return null;
    }

    /**
     * 返回协议请求的Url。此参数可以通过函数url()设置.派生类可以重载该函数。
     *
     * @return Url, 如果未指定则返回null
     */
    protected String getRequestUrl() {
        String rootUrl = getRootUrl();
        if (_url == null || _url.length() == 0) return rootUrl;
        if (rootUrl == null || rootUrl.length() == 0) return _url;
        if (!rootUrl.endsWith("/") && !_url.startsWith("/")) return rootUrl + "/" + _url;
        else if (rootUrl.endsWith("/") && _url.startsWith("/")) return rootUrl + _url.substring(1);
        else return rootUrl + _url;
    }

    /**
     * 返回协议请求的HTTP头，此参数可以通过函数headers()设置。派生类可以重载该函数。
     *
     * @return HTTP头，如果未指定则返回null
     */
    protected Map<String, String> getRequestHeaders() {
        return _headers;
    }

    /**
     * 返回协议请求的HTTP参数，此参数可以通过函数params()设置。派生类可以重载该函数。
     *
     * @return HTTP参数，如果未指定则返回null
     */
    protected Map<String, Object> getRequestParams() {
        return _params;
    }

    /**
     * 工具函数：根据HTTP请求的url和params，生成HTTP GET的带Query String的url
     *
     * @param url
     * @param params
     * @return 带Query String的url
     */
    protected String getHttpGetUrl(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) return url;

        StringBuilder sb = new StringBuilder(url);
        // Append '?' or '&' if necessary
        if (sb.indexOf("?") < 0) sb.append('?');
        else if (sb.charAt(sb.length() - 1) != '&') sb.append('&');
        // Append params to url
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (name == null || name.length() == 0) continue;

            sb.append(name.trim());
            sb.append('=');
            try {
                if (value != null) sb.append(URLEncoder.encode(value.toString().trim(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
            sb.append('&');
        }
        // delete last '&'
        if (sb.charAt(sb.length() - 1) == '&') sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 工具函数：根据HTTP请求的url和params，生成缓存HTTP应答消息的Key。派生类可以重载该函数。
     *
     * @param url
     * @param params
     * @return 缓存HTTP应答消息的Key。缺省返回请求的网络url
     */
    protected String getHttpCacheUrl(String url, Map<String, Object> params) {
        return _httpNetworkUrl;
    }

    /**
     * 从HTTP返回的数据中得到数据的Json对象。派生类可以重载该函数
     *
     * @param data     HTTP返回的data数据
     * @param encoding 编码格式
     * @return 数据的Json对象
     * @throws Exception 编码错误或Json解析错误
     */
    protected JsonElement getResultJson(byte[] data, String encoding) throws Exception {
        return new JsonParser().parse(new String(data, encoding));
    }

    /**
     * 将HTTP返回的数据转换成对应对象。此函数会调用getResultJson获取转换后的Json对象，派生类可重载getResultJson以获取特定节点的Json。
     *
     * @param url      HTTP请求的Cache URL，不是实际HTTP请求的networkUrl。
     * @param type     转换的目标对象的类型
     * @param encoding 返回的HTTP数据的编码格式
     * @param data     返回的HTTP数据
     * @param status   HTTP请求的状态
     * @param <T>      转换的目标对象
     * @return null - 转换失败；其他 - 转换成功，返回转换的对象。
     */
    @Override
    public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {

        try {
            // 如果数据是以目标类名称开始的字符串，则数据是缓存的setCacheResult的数据
            String dataString = new String(data, encoding);
            if (dataString.startsWith(type.getName())) {
                return gsonBuilder.create().fromJson(dataString.substring(_transformType.getName().length()), type);
            }
            // 将HTTP返回的或缓存的数据转换成对应对象
            JsonElement json = getResultJson(data, encoding);
            if (json == null) return null;
            if ((_transformOption & OPTION_GET_CHILD_OBJECT) != 0) {
                Set<Map.Entry<String, JsonElement>> entries = json.getAsJsonObject().entrySet();
                if (entries.size() == 0) return null;
                json = entries.iterator().next().getValue();
                if (json == null) return null;
            }
            if ((_transformOption & OPTION_GET_FIRST_OBJECT) != 0) {
                JsonArray array = json.getAsJsonArray();
                if (array.size() == 0) return null;
                json = array.get(0);
                if (json == null) return null;
            }
            return gsonBuilder.create().fromJson(json, type);
        } catch (Exception e) {
            AQUtility.debug("transform Exception : cls=" + type + " url=" + url);
            AQUtility.debug(e);
            AQUtility.debug("data", data != null ? new String(data) : "null");
            return null;
        }
    }
}
