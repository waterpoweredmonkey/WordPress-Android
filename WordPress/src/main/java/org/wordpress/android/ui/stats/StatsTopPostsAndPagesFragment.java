package org.wordpress.android.ui.stats;

import android.widget.ArrayAdapter;

import org.wordpress.android.R;
import org.wordpress.android.ui.stats.adapters.PostsAndPagesAdapter;
import org.wordpress.android.ui.stats.models.PostModel;
import org.wordpress.android.ui.stats.models.TopPostsAndPagesModel;
import org.wordpress.android.ui.stats.service.StatsService;

import java.util.ArrayList;
import java.util.List;


public class StatsTopPostsAndPagesFragment extends StatsAbstractListFragment {
    public static final String TAG = StatsTopPostsAndPagesFragment.class.getSimpleName();

    private TopPostsAndPagesModel topPostsAndPagesModel = null;

    @SuppressWarnings("unused")
    public void onEventMainThread(StatsEvents.TopPostsSectionUpdated event) {
        if (!shouldUpdateFragmentOnEvent(event)) {
            return;
        }

        mGroupIdToExpandedMap.clear();
        topPostsAndPagesModel = event.mTopPostsAndPagesModel;

        updateUI();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(StatsEvents.SectionUpdateError event) {
        if (!shouldUpdateFragmentOnErrorEvent(event)) {
            return;
        }

        topPostsAndPagesModel = null;
        mGroupIdToExpandedMap.clear();
        showErrorUI(event.mError);
    }

    @Override
    protected void updateUI() {
        if (!isAdded()) {
            return;
        }

        if (topPostsAndPagesModel == null) {
            showHideNoResultsUI(true);
            return;
        }

        if (hasTopPostsAndPages()) {
            List<PostModel> postViews = topPostsAndPagesModel.getTopPostsAndPages();
            ArrayAdapter adapter = new PostsAndPagesAdapter(getActivity(), postViews);
            StatsUIHelper.reloadLinearLayout(getActivity(), adapter, mList, getMaxNumberOfItemsToShowInList());
            showHideNoResultsUI(false);
        } else {
            showHideNoResultsUI(true);
        }
    }

    private boolean hasTopPostsAndPages() {
        return topPostsAndPagesModel != null && topPostsAndPagesModel.hasTopPostsAndPages();
    }

    private List<PostModel> getTopPostsAndPages() {
        if (!hasTopPostsAndPages()) {
            return new ArrayList<PostModel>(0);
        }
        return topPostsAndPagesModel.getTopPostsAndPages();
    }

    @Override
    protected boolean isViewAllOptionAvailable() {
        return hasTopPostsAndPages() && getTopPostsAndPages().size() > MAX_NUM_OF_ITEMS_DISPLAYED_IN_LIST;
    }

    @Override
    protected boolean isExpandableList() {
        return false;
    }

    @Override
    protected int getEntryLabelResId() {
        return R.string.stats_entry_posts_and_pages;
    }

    @Override
    protected int getTotalsLabelResId() {
        return R.string.stats_totals_views;
    }

    @Override
    protected int getEmptyLabelTitleResId() {
        return R.string.stats_empty_top_posts_title;
    }

    @Override
    protected int getEmptyLabelDescResId() {
        return R.string.stats_empty_top_posts_desc;
    }

    @Override
    protected StatsService.StatsEndpointsEnum[] getSectionsToUpdate() {
        return new StatsService.StatsEndpointsEnum[]{
                StatsService.StatsEndpointsEnum.TOP_POSTS
        };
    }

    @Override
    public String getTitle() {
        return getString(R.string.stats_view_top_posts_and_pages);
    }
}
