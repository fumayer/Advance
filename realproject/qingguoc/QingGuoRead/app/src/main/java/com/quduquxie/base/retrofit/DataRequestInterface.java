package com.quduquxie.base.retrofit;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.bean.Initialize;
import com.quduquxie.base.bean.MainContent;
import com.quduquxie.base.bean.Splash;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.retrofit.bean.BlanketResult;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.community.bean.UpdateShelf;
import com.quduquxie.model.DownloadShelfResult;
import com.quduquxie.model.creation.LiteratureList;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public interface DataRequestInterface {

    //获取版本更新
    @GET("/v1/version/last")
    Flowable<BlanketResult<Update>> loadAppUpdateInformation();










    //获取书架默认书籍
    @GET("/v2/bookshelves")
    Flowable<CommunalResult<ArrayList<Book>>> loadDefaultBooks();

    //启动时刷新令牌、注册设备
    @FormUrlEncoded
    @POST("/v1/supports")
    Flowable<CommunalResult<Initialize>> initializeParameter(@FieldMap Map<String, String> information);

    //启动页推荐
    @GET("/v2/chakra/first-screen")
    Flowable<CommunalResult<Splash>> loadSplashRecommend();

    //获取封面信息
    @GET("/v2/books/{book_id}")
    Flowable<CommunalResult<Book>> loadCoverInformation(@Path("book_id") String id);

    @GET("/v2/choices/{key}")
    Flowable<CommunalResult<MainContent>> loadSelectedData(@Path("key") String key);

    @GET("/v2/library/{key}")
    Flowable<CommunalResult<MainContent>> loadLibraryData(@Path("key") String key);

    //榜单
    @GET("{uri}/{date}")
    Flowable<CommunalResult<ArrayList<Book>>> loadBillboardData(@Path(value = "uri", encoded = true) String uri, @Path(value = "date", encoded = true) String date, @Query("q") String q, @Query("page") int page, @Query("limit") int limit);

    //列表
    @GET("{uri}")
    Flowable<CommunalResult<ArrayList<Book>>> loadTabulationData(@Path(value = "uri", encoded = true) String uri, @Query("q") String q, @Query("page") int page, @Query("limit") int limit);

    //获取书籍目录
    @GET("/v2/books/{id}/chapters")
    Flowable<CommunalResult<ArrayList<Chapter>>> loadBookCatalog(@Path("id") String id, @Query("start") int start);


    //分类榜单
    @GET("/v2/books/categories")
    Flowable<CommunalResult<ArrayList<Book>>> loadCategoryTabulation(@Query("word") String word, @Query("page") int page, @Query("limit") int limit);


    /**************************************用户相关**************************************/
    //上传用户书架信息
    @POST("/v1/sns/auth/up_shelf")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Flowable<CommunalResult<UpdateShelf>> updateUserBookShelf(@Body RequestBody requestBody);

    //下载用户书架信息
    @GET("/v1/sns/auth/down_shelf")
    Flowable<CommunalResult<DownloadShelfResult>> downloadUserBookShelf();






    /**************************************写作相关**************************************/
    //获取创作列表
    @GET("/v1/ugc/books")
    Flowable<CommunalResult<LiteratureList>> loadLiteratureList();
}