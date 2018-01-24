package cn.net.xinyi.xmjt.v527.data.remote.retrofit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.net.xinyi.xmjt.v527.base.model.ResponseData;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by Fracesuit on 2017/10/22.
 */

public interface CacheProvider {
    //案件性质--字典
    @LifeCache(duration = 10, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<ResponseData<String>>> getTxl(Observable<ResponseData<String>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);
    //案件性质--字典
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<List<GzrzTypeModel>>> getDict(Observable<List<GzrzTypeModel>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
