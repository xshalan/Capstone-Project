package app.com.shalan.spacego.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import app.com.shalan.spacego.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by noura on 13/08/2017.
 */

public class reviewViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView timeStamp;
    public TextView review;
    public CircleImageView userImage;
    public RatingBar ratingBar;
    public View mView;

    public reviewViewHolder(View itemView) {
        super(itemView);
        username = (TextView) itemView.findViewById(R.id.comment_username);
        timeStamp = (TextView) itemView.findViewById(R.id.comment_Time);
        userImage = (CircleImageView) itemView.findViewById(R.id.comment_profileImage);
        review = (TextView) itemView.findViewById(R.id.userReview);
        ratingBar = (RatingBar) itemView.findViewById(R.id.userRating);

        mView = itemView;
    }


}
