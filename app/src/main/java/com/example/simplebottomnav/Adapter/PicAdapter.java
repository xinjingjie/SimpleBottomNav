package com.example.simplebottomnav.Adapter;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.GetPicKey;
import com.example.simplebottomnav.repository.LoadPic;
import com.example.simplebottomnav.viewmodel.PicViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import io.supercharge.shimmerlayout.ShimmerLayout;


public class PicAdapter extends ListAdapter<Picture, RecyclerView.ViewHolder> {
    private PicViewModel viewModel;
    static int NORMAL_VIEW_TYPE = 0;
    static int FOOTER_VIEW_TYPE = 1;
    private int footer_state = LoadPic.CAN_LOAD_MORE;
    public final static int CARD_VIEW = 0;
    public final static int NORMAL_VIEW = 1;
    private int style;
    public void setFooter_state(int footer_state) {
        this.footer_state = footer_state;
    }

    SharedPreferences likedPreferences;

    public PicAdapter(PicViewModel viewModel, int style) {
        super(new DiffUtil.ItemCallback<Picture>() {
            @Override
            public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.getP_id() == newItem.getP_id();

            }
        });
        this.viewModel = viewModel;
        this.style = style;
        if (likedPreferences == null) {
            Log.d("likedPreferences", "PicAdapter: is null");
        }
//        if (likedPreferences==null){
//            SharedPreferences.Editor editor=viewModel.getApplication().getSharedPreferences("liked_pref",viewModel.getApplication().getApplicationContext().MODE_PRIVATE).edit();
//            editor.clear();
//            editor.apply();
//        }

        likedPreferences = viewModel.getApplication().getSharedPreferences(MainActivity.liked_prefName, viewModel.getApplication().getApplicationContext().MODE_PRIVATE);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            Log.d("FOOTER", "getItemViewType: ");
            return FOOTER_VIEW_TYPE;
        } else {
            return NORMAL_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view;
        if (viewType == NORMAL_VIEW_TYPE) {
            if (style == CARD_VIEW) {
                view = layoutInflater.inflate(R.layout.homepic_cell_card, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.homepic_cell_normal, parent, false);


            }
            holder = new PicViewHolder(view);
            PicViewHolder picHolder = (PicViewHolder) holder;

            picHolder.shimmerLayout.setOnClickListener(v -> {
                Log.d("TAG", "onClick: ");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isProfile", false);
                bundle.putParcelable("Detail_Pic", getItem(picHolder.getAdapterPosition()));
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_homeFragment_to_detailPicFragment2, bundle);
                //   navController.navigate(R.id.action_searchFragment_to_detailPicFragment2, bundle);

            });
            /*
            下拉选项   optionMenu或者ContextMenu
             */
//            picHolder.spinner_button.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Log.d("TAG", "onItemSelected: +++++++++");
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });



        } else {
            final View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_pic_footer, parent, false);
            holder = new FooterViewHolder(view2);
//            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//            layoutParams.setFullSpan(true);

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder original_holder, int position) {
        //防止数据错乱粗暴方法
        // original_holder.setIsRecyclable(false);
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) original_holder;
            Log.d("TAG", "onBindViewHolder: " + footer_state);
            switch (footer_state) {
                case LoadPic.CAN_LOAD_MORE:
                    footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                    footerViewHolder.loadMessage.setText("正在加载...");
                    break;
                case LoadPic.NO_MORE_DATA:
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.loadMessage.setText("已经加载全部");
                    break;
                case LoadPic.NETWORK_ERROR:
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.loadMessage.setText("网络错误，稍后重试");
                    footerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PicViewModel picViewModel = viewModel;
                            picViewModel.resetData();
                            picViewModel.setPhotoListLive(LoadPic.FIND_TYPE_RECOMMEND, GetPicKey.getLeastKey());
                        }
                    });
                    break;
            }
        } else {
            final PicViewHolder holder = (PicViewHolder) original_holder;
            final ShimmerLayout shimmerLayout = holder.shimmerLayout;
            if (shimmerLayout == null) {
                Log.d("did", "onBindViewHolder: is null");
            }
            shimmerLayout.setShimmerColor(0x55FFFFFF);
            shimmerLayout.setShimmerAngle(0);
            shimmerLayout.startShimmerAnimation();
            holder.likeText.setText(String.valueOf(getItem(position).getLikes()));
            holder.picDescription.setText(getItem(position).getContent());
            holder.messageText.setText(String.valueOf(getItem(position).getComments()));
            holder.userName.setText(getItem(position).getUsername());
            Glide.with(holder.imageView)
                    .load(getItem(position).getProfile_picture()
                    ).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.userButton);
//            holder.likeButton.setImageResource(R.drawable.ic_favorite_border_gray_24dp);
            int pid = getItem(position).getP_id();
            if (likedPreferences.contains("" + pid)) {
                holder.isLike = true;
                if (!likedPreferences.contains("isFresh")) {
                    holder.likeText.setText(String.valueOf(Integer.parseInt(holder.likeText.getText().toString()) + 1));
                }
                holder.likeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
            }

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    点赞!
                     */

                    int uid = viewModel.getSavedStateHandle().get("UID");
                    //String username = viewModel.getSavedStateHandle().get("username");

                    //把点赞图片id放到prefences里
                    SharedPreferences.Editor editor = likedPreferences.edit();

                    if (holder.isLike) {
                        holder.likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        holder.likeText.setText(String.valueOf(Integer.parseInt(holder.likeText.getText().toString()) - 1));
                        if (!viewModel.getSavedStateHandle().contains("HAVADATA")) {
                            viewModel.getSavedStateHandle().set("HAVADATA", true);
                        }
                        if (viewModel.getSavedStateHandle().contains("" + pid)) {
                            viewModel.getSavedStateHandle().remove("" + pid);
                        }
                        holder.isLike = false;
                        editor.remove("" + pid);
                        editor.apply();
                    } else {
                        if (!viewModel.getSavedStateHandle().contains("HAVADATA")) {
                            viewModel.getSavedStateHandle().set("HAVADATA", true);
                        }
                        holder.likeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                        holder.likeText.setText(String.valueOf(Integer.parseInt(holder.likeText.getText().toString()) + 1));
                        viewModel.getSavedStateHandle().set("" + pid, "" + uid);
                        holder.isLike = true;
                        editor.putBoolean("" + pid, true);
                        editor.apply();
                    }
                }
            });

 /*
            查看评论
             */
            holder.viewComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("pid", getItem(position).getP_id());
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homeFragment_to_detailCommentsFragment, bundle);
                }
            });

            Glide.with(holder.itemView)
                    .load(getItem(position).getLocation())
                    .placeholder(R.drawable.ic_photo_gray_24dp)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            shimmerLayout.stopShimmerAnimation();
                            return false;
                        }
                    })
                    .into(holder.imageView);

             /*
        点击头像
         */

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("uid", getItem(position).getUser_id());
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homeFragment_to_otherUserFragment, bundle);
                }
            };
            holder.userButton.setOnClickListener(listener);

            /*
            点击username
             */
            holder.userName.setOnClickListener(listener);

        }


    }

    // public abstract static class PicViewHolder
    static class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ShimmerLayout shimmerLayout;
        ImageButton likeButton, messageButton;
        CircleImageView userButton;
        TextView likeText, messageText, picDescription, userName, viewComments;
        ConstraintLayout function_layout;
        Spinner spinner_button;
        boolean isLike = false;

        PicViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerLayout = itemView.findViewById(R.id.shimmersw);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);
            messageButton = itemView.findViewById(R.id.messageButton);
            likeText = itemView.findViewById(R.id.likeText);
            messageText = itemView.findViewById(R.id.messageText);
            picDescription = itemView.findViewById(R.id.pic_description);
            function_layout = itemView.findViewById(R.id.function_layout);
            userButton = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.user_name);
            spinner_button = itemView.findViewById(R.id.spinner_button);
            viewComments = itemView.findViewById(R.id.viewComments);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout footer_layout;
        ProgressBar progressBar;
        TextView loadMessage;

        private FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            footer_layout = itemView.findViewById(R.id.footer_layout);
            progressBar = itemView.findViewById(R.id.load_progressBar);
            loadMessage = itemView.findViewById(R.id.load_text);
        }
    }
}
