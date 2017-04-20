package com.zcm.router.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zcm.router.route.BrowserRoute;
import com.zcm.router.route.IRoute;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.zcm.router.utils.UrlUtils.getScheme;

/**
 * Created by kris on 16/3/17.
 */
public class BrowserRouter extends BaseRouter {
    private static final Set<String> SCHEMES_CAN_OPEN = new LinkedHashSet<>();

    static BrowserRouter mBrowserRouter = new BrowserRouter();  //浏览器


    static {
        SCHEMES_CAN_OPEN.add("https");
        SCHEMES_CAN_OPEN.add("http");
    }

    public static BrowserRouter getInstance(){
        return mBrowserRouter;
    }

    protected boolean open(Context context, IRoute route){
        if(doOnInterceptor(context, route.getUrl())){
            return true;
        }
        Uri uri = Uri.parse(route.getUrl());
        Intent intent = new Intent("com.zcm.action.browser", uri);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory("android.intent.category.DEFAULT");
        context.startActivity(intent);
        return true;
    }

    @Override
    public boolean open(IRoute route) {
        return open(mBaseContext, route);
    }

    @Override
    public boolean open(String url) {
        open(getRoute(url));
        return true;
    }

    @Override
    public boolean open(Context context, String url) {
        return open(context, getRoute(url));
    }

    @Override
    public BrowserRoute getRoute(String url) {
        return new BrowserRoute.Builder(this)
                .setUrl(url)
                .build();
    }

    @Override
    public boolean canOpenTheRoute(IRoute route) {
        return getCanOpenRoute().equals(route.getClass());
    }

    @Override
    public boolean canOpenTheUrl(String url) {
        return SCHEMES_CAN_OPEN.contains(getScheme(url));
    }

    @Override
    public Class<? extends IRoute> getCanOpenRoute() {
        return BrowserRoute.class;
    }



    private boolean doOnInterceptor(Context context, String url){
        if(interceptor != null){
            return interceptor.intercept(context, url);
        }
        return false;
    }
}
