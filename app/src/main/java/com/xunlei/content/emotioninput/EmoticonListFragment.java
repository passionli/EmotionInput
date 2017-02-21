package com.xunlei.content.emotioninput;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EmoticonListFragment extends Fragment {
    private static final String TAG = "EmoticonListFragment";
    @BindView(R.id.recyclerVew)
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    EmotionListAdapter mAdapter;

    List<EmoticonBean> mData = new ArrayList<>();
    private EmoticonOnClickListener mEmoticonOnClickListener;

    public EmoticonListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        for (Map.Entry<String, Integer> entry : EmotionData.sEmotionMap.entrySet()) {
            mData.add(new EmoticonBean(entry.getKey(), entry.getKey(), entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emotion_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mGridLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new EmotionListAdapter(mEmoticonOnClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static Fragment newInstance(int position, EmoticonOnClickListener listener) {
        EmoticonListFragment fragment = new EmoticonListFragment();
        fragment.setEmotionOnClickListener(listener);
        return fragment;
    }

    private void setEmotionOnClickListener(EmoticonOnClickListener listener) {
        mEmoticonOnClickListener = listener;
    }

    private class EmotionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater mInflater;
        EmoticonOnClickListener mListener;

        public EmotionListAdapter(EmoticonOnClickListener emoticonOnClickListener) {
            mInflater = LayoutInflater.from(getContext());
            mListener = emoticonOnClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.emotion_item, null);
            return new MyEmotionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final EmoticonBean bean = mData.get(position);
            final MyEmotionViewHolder vh = (MyEmotionViewHolder) holder;
            //TODO glide load emotion
            vh.imageView.setImageResource(bean.resId);
            vh.imageView.setTag(R.string.emotion_drag_key, bean.id);
            vh.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call back high level mListener
                    mListener.onClick(bean);
                }
            });
            vh.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //这里应该传emotion的key，默认0号位置放key
                    ClipData.Item item = new ClipData.Item((String) v.getTag(R.string.emotion_drag_key));

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    ClipData dragData = new ClipData((CharSequence) v.getTag(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    // Instantiates the drag shadow builder.
                    View.DragShadowBuilder myShadow = new EmoticonDragShadowBuilder(v);

                    // Starts the drag

                    v.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0          // flags (not currently used, set to 0)
                    );
                    return true;
                }
            });

            vh.textView.setText(bean.title);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


    protected class MyEmotionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;


        public MyEmotionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }
}
