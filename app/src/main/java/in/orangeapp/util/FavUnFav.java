package in.orangeapp.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import in.orangeapp.realestate.MyApplication;
import in.orangeapp.realestate.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FavUnFav {

    private ProgressDialog pDialog;
    private Activity mContext;

    public FavUnFav(Activity context) {
        this.mContext = context;
        pDialog = new ProgressDialog(mContext);
    }

    public void userFav(final String recId, final FavClickListener saveClickListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(mContext));
        jsObj.addProperty("method_name", "add_favourite");
        jsObj.addProperty("post_id", recId);
        jsObj.addProperty("user_id", MyApplication.getInstance().getUserId());
        params.put("data", API.toBase64(jsObj.toString()));
        client.post(Constant.API_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dismissProgressDialog();

                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);

                    Toast.makeText(mContext, mainJson.getString(Constant.MSG), Toast.LENGTH_SHORT).show();
                    saveClickListener.onItemClick(mainJson.getBoolean(Constant.FAV_TAG), mainJson.getString(Constant.MSG));
                    Events.FavReal favReal = new Events.FavReal();
                    favReal.setReId(recId);
                    favReal.setFav(mainJson.getBoolean(Constant.FAV_TAG));
                    GlobalBus.getBus().post(favReal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dismissProgressDialog();
            }

        });
    }

    private void showProgressDialog() {
        pDialog.setMessage(mContext.getString(R.string.loading_title));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }
}
