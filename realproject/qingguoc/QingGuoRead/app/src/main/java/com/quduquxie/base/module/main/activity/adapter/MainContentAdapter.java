package com.quduquxie.base.module.main.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Category;
import com.quduquxie.base.bean.Direct;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.listener.BannerListener;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.listener.CategoryListener;
import com.quduquxie.base.listener.DirectListener;
import com.quduquxie.base.listener.MainContentListener;
import com.quduquxie.base.viewholder.BookInformationHolder;
import com.quduquxie.base.viewholder.FillerHolder;
import com.quduquxie.base.viewholder.MainAuthorRecommendHolder;
import com.quduquxie.base.viewholder.MainBannerHolder;
import com.quduquxie.base.viewholder.MainEditorRecommendHolder;
import com.quduquxie.base.viewholder.MainHorizontalListHolder;
import com.quduquxie.base.viewholder.MainLibraryCategoryHolder;
import com.quduquxie.base.viewholder.MainLibraryJumpHolder;
import com.quduquxie.base.viewholder.MainModuleHeadHolder;
import com.quduquxie.base.viewholder.MainPromptSearchHolder;
import com.quduquxie.base.viewholder.MainSelectedCategoryHolder;
import com.quduquxie.base.viewholder.MainSelectedJumpHolder;
import com.quduquxie.base.viewholder.MainTopHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<MainContentItem> mainContentItems;
    private LayoutInflater layoutInflater;

    private BookListener bookListener;

    private DirectListener directListener;
    private BannerListener bannerListener;
    private CategoryListener categoryListener;
    private MainContentListener mainContentListener;

    public static final int TYPE_BANNER = 0x80;
    public static final int TYPE_EDITOR_RECOMMEND = 0x81;
    public static final int TYPE_AUTHOR_RECOMMEND = 0x82;
    public static final int TYPE_SELECTED_CATEGORY = 0x83;
    public static final int TYPE_SELECTED_JUMP = 0x84;
    public static final int TYPE_LIBRARY_JUMP = 0x85;
    public static final int TYPE_CHANNEL_TOP = 0x86;
    public static final int TYPE_HORIZONTAL_LIST = 0x87;
    public static final int TYPE_BOOK_VERTICAL = 0x88;


    public static final int TYPE_FILLER_DIVIDER = 0x89;
    public static final int TYPE_FILLER_SHADOW = 0x90;

    public static final int TYPE_LIBRARY_CATEGORY = 0x65;
    public static final int TYPE_LIBRARY_CATEGORY_HOT = 0x66;

    public static final int TYPE_PROMPT_SEARCH = 0x21;

    public static final int TYPE_MODULE_HEAD = 0x20;

    public static final int TYPE_FILLER = 0x88;

    public MainContentAdapter(Context context, ArrayList<MainContentItem> mainContentItems) {
        this.contextReference = new WeakReference<>(context);
        this.mainContentItems = mainContentItems;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new MainBannerHolder(layoutInflater.inflate(R.layout.layout_item_main_banner, parent, false));
            case TYPE_EDITOR_RECOMMEND:
                return new MainEditorRecommendHolder(layoutInflater.inflate(R.layout.layout_item_main_editor_recommend, parent, false));
            case TYPE_AUTHOR_RECOMMEND:
                return new MainAuthorRecommendHolder(layoutInflater.inflate(R.layout.layout_item_main_author_recommend, parent, false));
            case TYPE_SELECTED_CATEGORY:
                return new MainSelectedCategoryHolder(layoutInflater.inflate(R.layout.layout_item_main_selected_category, parent, false));
            case TYPE_SELECTED_JUMP:
                return new MainSelectedJumpHolder(layoutInflater.inflate(R.layout.layout_item_main_selected_jump, parent, false));
            case TYPE_LIBRARY_JUMP:
                return new MainLibraryJumpHolder(layoutInflater.inflate(R.layout.layout_item_main_library_jump, parent, false));
            case TYPE_HORIZONTAL_LIST:
                return new MainHorizontalListHolder(layoutInflater.inflate(R.layout.layout_item_main_horizontal_list, parent, false));
            case TYPE_MODULE_HEAD:
                return new MainModuleHeadHolder(layoutInflater.inflate(R.layout.layout_item_main_module_head, parent, false));
            case TYPE_BOOK_VERTICAL:
                return new BookInformationHolder(layoutInflater.inflate(R.layout.layout_item_book_information, parent, false));
            case TYPE_CHANNEL_TOP:
                return new MainTopHolder(layoutInflater.inflate(R.layout.layout_item_main_top, parent, false));
            case TYPE_LIBRARY_CATEGORY:
                return new MainLibraryCategoryHolder(layoutInflater.inflate(R.layout.layout_item_main_library_category, parent, false));
            case TYPE_LIBRARY_CATEGORY_HOT:
                return new MainLibraryCategoryHolder(layoutInflater.inflate(R.layout.layout_item_main_library_hot_category, parent, false));
            case TYPE_FILLER_SHADOW:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler_shadow, parent, false));
            case TYPE_FILLER_DIVIDER:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_item_main_filler_divider, parent, false));
            case TYPE_PROMPT_SEARCH:
                return new MainPromptSearchHolder(layoutInflater.inflate(R.layout.layout_item_main_prompt_search, parent, false));
            default:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final MainContentItem mainContentItem = mainContentItems.get(position);
        if (mainContentItem == null) {
            return;
        }

        if (viewHolder instanceof MainBannerHolder) {
            ((MainBannerHolder) viewHolder).initializeBannerData(contextReference.get(), mainContentItem.bannerList, bannerListener);

            if (((MainBannerHolder) viewHolder).main_banner_content != null) {
                ((MainBannerHolder) viewHolder).main_banner_content.setOffscreenPageLimit(1);
            }
        } else if (viewHolder instanceof MainEditorRecommendHolder) {

            ((MainEditorRecommendHolder) viewHolder).initializeView(contextReference.get(), mainContentItem, bookListener);
            ((MainEditorRecommendHolder) viewHolder).main_mode_head_detailed.setTag(R.id.click_object, mainContentItem);
            ((MainEditorRecommendHolder) viewHolder).main_mode_head_detailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        MainContentItem clickedMainContentItem = (MainContentItem) view.getTag(R.id.click_object);
                        if (clickedMainContentItem != null) {
                            mainContentListener.startTabulationActivity(clickedMainContentItem.title, clickedMainContentItem.uri);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof MainAuthorRecommendHolder) {
            ((MainAuthorRecommendHolder) viewHolder).initializeView(contextReference.get(), mainContentItem.bookList, bookListener);
        } else if (viewHolder instanceof MainSelectedCategoryHolder) {
            ((MainSelectedCategoryHolder) viewHolder).initializeView(contextReference.get(), mainContentItem.categories, categoryListener);
        } else if (viewHolder instanceof MainSelectedJumpHolder) {
            ((MainSelectedJumpHolder) viewHolder).initializeView(mainContentItem.directs);
            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_first.setTag(R.id.click_object, mainContentItem.directs.get(0));
            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (directListener != null) {
                        Direct clickedDirect = (Direct) view.getTag(R.id.click_object);
                        if (clickedDirect != null) {
                            directListener.onClickedDirect(clickedDirect);
                        }
                    }
                }
            });

            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_second.setTag(R.id.click_object, mainContentItem.directs.get(1));
            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (directListener != null) {
                        Direct clickedDirect = (Direct) view.getTag(R.id.click_object);
                        if (clickedDirect != null) {
                            directListener.onClickedDirect(clickedDirect);
                        }
                    }
                }
            });

            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_third.setTag(R.id.click_object, mainContentItem.directs.get(2));
            ((MainSelectedJumpHolder) viewHolder).main_selected_jump_third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (directListener != null) {
                        Direct clickedDirect = (Direct) view.getTag(R.id.click_object);
                        if (clickedDirect != null) {
                            directListener.onClickedDirect(clickedDirect);
                        }
                    }
                }
            });

        } else if (viewHolder instanceof MainLibraryJumpHolder) {
            ((MainLibraryJumpHolder) viewHolder).initializeView(contextReference.get(), mainContentItem.direct);
            ((MainLibraryJumpHolder) viewHolder).library_jump.setTag(R.id.click_object, mainContentItem.direct);
            ((MainLibraryJumpHolder) viewHolder).library_jump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (directListener != null) {
                        Direct clickedDirect = (Direct) view.getTag(R.id.click_object);
                        if (clickedDirect != null) {
                            directListener.onClickedDirect(clickedDirect);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof MainHorizontalListHolder) {

            ((MainHorizontalListHolder) viewHolder).initializeView(contextReference.get(), mainContentItem, bookListener);

            ((MainHorizontalListHolder) viewHolder).main_mode_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        MainContentItem clickedMainContentItem = (MainContentItem) view.getTag(R.id.click_object);
                        if (clickedMainContentItem != null) {
                            mainContentListener.startTabulationActivity(clickedMainContentItem.title, clickedMainContentItem.uri);
                        }
                    }
                }
            });

            ((MainHorizontalListHolder) viewHolder).main_mode_head_detailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        MainContentItem clickedMainContentItem = (MainContentItem) view.getTag(R.id.click_object);
                        if (clickedMainContentItem != null) {
                            mainContentListener.startTabulationActivity(clickedMainContentItem.title, clickedMainContentItem.uri);
                        }
                    }
                }
            });

        } else if (viewHolder instanceof BookInformationHolder) {
            ((BookInformationHolder) viewHolder).initializeView(contextReference.get(), mainContentItem.book, false, true);
            ((BookInformationHolder) viewHolder).book_information.setTag(R.id.click_object, mainContentItem.book);
            ((BookInformationHolder) viewHolder).book_information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            bookListener.onClickedBook(clickedBook);
                        }
                    }
                }
            });

            ((BookInformationHolder) viewHolder).book_information_more.setTag(R.id.click_object, mainContentItem.book);
            ((BookInformationHolder) viewHolder).book_information_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            mainContentListener.showPromptView(view, clickedBook);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof MainLibraryCategoryHolder) {
            ((MainLibraryCategoryHolder) viewHolder).initializeView(contextReference.get(), mainContentItem.title, mainContentItem.category);
            ((MainLibraryCategoryHolder) viewHolder).library_category.setTag(R.id.click_object, mainContentItem.category);
            ((MainLibraryCategoryHolder) viewHolder).library_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (categoryListener != null) {
                        Category category = (Category) view.getTag(R.id.click_object);
                        if (category != null) {
                            categoryListener.onClickedCategory(category);
                        }
                    }
                }
            });

        } else if (viewHolder instanceof MainModuleHeadHolder) {
            ((MainModuleHeadHolder) viewHolder).initializeView(mainContentItem);
            ((MainModuleHeadHolder) viewHolder).module_head.setTag(R.id.click_object, mainContentItem);
            ((MainModuleHeadHolder) viewHolder).module_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        MainContentItem clickedMainContentItem = (MainContentItem) view.getTag(R.id.click_object);
                        if (clickedMainContentItem != null) {
                            mainContentListener.startTabulationActivity(mainContentItem.title, mainContentItem.uri);
                        }
                    }
                }
            });

        } else if (viewHolder instanceof MainTopHolder) {
            ((MainTopHolder) viewHolder).initializeView(contextReference.get(), mainContentItem, bookListener);
        } else if (viewHolder instanceof MainPromptSearchHolder) {
            ((MainPromptSearchHolder) viewHolder).prompt_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainContentListener != null) {
                        mainContentListener.startSearchActivity();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mainContentItems == null || mainContentItems.isEmpty()) {
            return 0;
        } else {
            return mainContentItems.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position > -1 && position < mainContentItems.size()) {
            return mainContentItems.get(position).type;
        } else {
            return TYPE_FILLER;
        }
    }

    public void setBookListener(BookListener bookListener) {
        this.bookListener = bookListener;
    }

    public void setBannerListener(BannerListener bannerListener) {
        this.bannerListener = bannerListener;
    }

    public void setDirectListener(DirectListener directListener) {
        this.directListener = directListener;
    }

    public void setCategoryListener(CategoryListener categoryListener) {
        this.categoryListener = categoryListener;
    }

    public void setMainContentListener(MainContentListener mainContentListener) {
        this.mainContentListener = mainContentListener;
    }

    public void recycle() {

    }
}