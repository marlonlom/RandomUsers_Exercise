package com.github.bconzi.random_users.listings;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bconzi.random_users.R;
import com.github.bconzi.random_users.model.network.data.RandomUser;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Random Users Listing Recycler Adapter class.
 *
 * @author marlonlom
 */
public class RandomUsersRecyclerAdapter extends RecyclerView.Adapter<RandomUsersRecyclerAdapter.ViewHolder> {

    private List<RandomUser> mItems = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing_users, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Change random users list items.
     *
     * @param newItems new list items.
     */
    void changeItems(final List<RandomUser> newItems) {
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(mItems, newItems));
        mItems.clear();
        mItems.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Get item from adapter.
     *
     * @param position item position.
     * @return random user item from adapter.
     */
    RandomUser getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Random users list item diff util callback class.
     *
     * @author marlonlom
     */
    static class DiffUtilCallback extends DiffUtil.Callback {

        private final List<RandomUser> oldItems;
        private final List<RandomUser> newItems;

        DiffUtilCallback(List<RandomUser> oldItems, List<RandomUser> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).hashCode() == newItems.get(newItemPosition).hashCode();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
        }
    }

    /**
     * Random users list item view holder class.
     *
     * @author marlonlom
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Binds random user data to this view holder.
         *
         * @param randomUser random user data.
         */
        void bindItem(final RandomUser randomUser) {
            Picasso.get().load(randomUser.getPicture().getLarge())
                    .fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into((ImageView) itemView.findViewById(R.id.imageView_item_thumb));

            ViewCompat.setTransitionName(itemView.findViewById(R.id.imageView_item_thumb),
                    randomUser.getName().getFullName());

            ((TextView) itemView.findViewById(R.id.textView_item_name)).setText(randomUser.getName().getFullName());
        }
    }

    /**
     * Random users list item touch listener class.
     *
     * @author marlonlom
     */
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                final ImageView thumbnailImageView = child.findViewById(R.id.imageView_item_thumb);
                clicklistener.onClick(child, rv.getChildAdapterPosition(child), thumbnailImageView);
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        /**
         * Random users list item click listener definition for touch listener class.
         *
         * @author marlonlom
         */
        interface ClickListener {
            void onClick(View view, int position, ImageView thumbnailImageView);

            void onLongClick(View view, int position);
        }
    }

    /**
     * Random users list grid item decoration class.
     *
     * @author marlonlom
     */
    static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        private int headerNum;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
            this.headerNum = headerNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view) - headerNum; // item position

            if (position >= 0) {
                int column = position % spanCount;

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount;
                    outRect.right = (column + 1) * spacing / spanCount;

                    if (position < spanCount) {
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing;
                } else {
                    outRect.left = column * spacing / spanCount;
                    outRect.right = spacing - (column + 1) * spacing / spanCount;
                    if (position >= spanCount) {
                        outRect.top = spacing;
                    }
                }
            } else {
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = 0;
                outRect.bottom = 0;
            }
        }
    }
}
