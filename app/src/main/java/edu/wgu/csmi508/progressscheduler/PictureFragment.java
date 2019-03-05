package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class PictureFragment extends Fragment {
    private static final String ARG_COURSE_ID = "course_id";

    private static final int REQUEST_CAMERA = 0;

    private Course mCourse;
    private ImageView mImageView;
    private Button mNewPictureButton;
    private File mPicture;

    public static PictureFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = CourseList.getCourseList(getActivity()).getCourse(courseId);
        mPicture = CourseList.getCourseList(getActivity()).getPicture(mCourse);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        mImageView = view.findViewById(R.id.photo);
        updateImage();

        mNewPictureButton = view.findViewById(R.id.new_picture_button);
        final Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mNewPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "edu.wgu.csmi508.progressscheduler.fileprovider", mPicture);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> infoList = getActivity().getPackageManager()
                        .queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo info : infoList) {
                    getActivity().grantUriPermission(info.activityInfo.packageName, uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CAMERA) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "edu.wgu.csmi508.progressscheduler.fileprovider", mPicture);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateImage();
        }
    }

    private void updateImage() {
        if (mPicture == null || !mPicture.exists()) {
            mImageView.setImageDrawable(null);
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            final Bitmap bitmap = BitmapFactory.decodeFile(mPicture.getPath(), options);
            mImageView.setImageBitmap(bitmap);
        }
    }
}
