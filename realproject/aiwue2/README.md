# TodayNews
一个仿今日头条的开源项目，是基于MVP+[RxJava](https://github.com/ReactiveX/RxJava)+[Retrofit](https://github.com/square/retrofit)

# Blog

[Android仿今日头条的开源项目](http://www.weyye.me/detail/my-project-today-news/)

#Apk

[点击下载](http://fir.im/np8w)

# 项目截图

![](/screenshot/01.png)

![](/screenshot/02.png)

![](/screenshot/03.png)

![](/screenshot/04.png)

![](/screenshot/05.png)

![](/screenshot/06.png)

![](/screenshot/07.png)

![](/screenshot/08.jpg)



# 第三方库
* [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* [ImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader)
* [Retrofit](https://github.com/square/retrofit)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [MultipleTheme](https://github.com/dersoncheng/MultipleTheme)
* [Gson](https://github.com/google/gson)
* [JieCaoVideoPlayer](https://github.com/lipangit/JieCaoVideoPlayer)

# 技术要点

* 主要是一些第三方库的使用
* 多种Item布局展示->[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* 日夜间模式切换->[MultipleTheme](https://github.com/dersoncheng/MultipleTheme)
* 新闻详情我采用的是RecyclerView添加头的方式添加WebView（当然是Adapter里面添加）,加载页面成功之后获取评论信息，点击评论图标滑动至评论第一条，这里我是调用`recyclerView.smoothScrollToPosition(1);`
* 视频播放我使用的是[JieCaoVideoPlayer](https://github.com/lipangit/JieCaoVideoPlayer),一群大牛封装的代码，底层实际使用[ijkplayer](https://github.com/Bilibili/ijkplayer),视频源均使用非正常手段获取，视频源地址分析请看我的另一篇博客[手撸一个今日头条视频下载器](http://www.weyye.me/detail/today-news-video/)
 
在使用[MultipleTheme](https://github.com/dersoncheng/MultipleTheme)的时候唯一的缺陷就是需要在布局里面大量使用到自定义控件，这对于我们的项目而言，布局看着很冗余，也有点恶心。。我有时候就在想，那我可不可以写原生控件，然后在特定的时机来个偷梁换柱换成我们的自定义控件呢？似乎好像是可以的，当我们加载布局的时候最终都会用`LayoutInflater`来加载，所以我打算从这里入手，看源码我发现有一个接口可以利用->`Factory`,这个接口有一个方法

``` java 
    public interface Factory {
        /**
         * Hook you can supply that is called when inflating from a LayoutInflater.
         * You can use this to customize the tag names available in your XML
         * layout files.
         * 
         * <p>
         * Note that it is good practice to prefix these custom names with your
         * package (i.e., com.coolcompany.apps) to avoid conflicts with system
         * names.
         * 
         * @param name Tag name to be inflated.
         * @param context The context the view is being created in.
         * @param attrs Inflation attributes as specified in XML file.
         * 
         * @return View Newly created view. Return null for the default
         *         behavior.
         */
        public View onCreateView(String name, Context context, AttributeSet attrs);
    }

```

果然功夫不负有心人，如果我们实现了这个接口，最终加载布局的时候那么就会调用`onCreateView`在这里面来实现偷梁换柱替换成我们的自定义控件

# TODO

* 加入未写界面以及功能
* 逻辑代码的整理

# 声明

这个属于个人开发作品，仅做学习交流使用，如用到实际项目还需多考虑其他因素如并发等，请多多斟酌。**诸位勿传播于非技术人员，拒绝用于商业用途，数据均属于非正常渠道获取，原作公司拥有所有权利。**

# License

	Copyright (C) 2017 WeyYe
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.