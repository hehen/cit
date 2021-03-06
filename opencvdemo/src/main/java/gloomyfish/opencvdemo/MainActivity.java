package gloomyfish.opencvdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.book.datamodel.AppConstants;
import com.book.datamodel.ChapterUtils;
import com.book.datamodel.ItemDto;
import com.book.datamodel.SectionsListViewAdaptor;

import org.opencv.android.OpenCVLoader;

@Route(path = "/opencvdemo/MainActivity")
public class MainActivity extends AppCompatActivity {
    private String CV_TAG = "OpenCV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opencvdemo_activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        iniLoadOpenCV();
        initListView();
    }

    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if(success) {
            Log.i(CV_TAG, "OpenCV Libraries loaded...");
        } else {
            Toast.makeText(this.getApplicationContext(), "WARNING: Could not load OpenCV Libraries!", Toast.LENGTH_LONG).show();
        }
    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.chapter_listView);
        final SectionsListViewAdaptor commandAdaptor = new SectionsListViewAdaptor(this);
        listView.setAdapter(commandAdaptor);
        commandAdaptor.getDataModel().addAll(ChapterUtils.getChapters());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDto dot = commandAdaptor.getDataModel().get(position);
                goSectionList(dot);
            }
        });
        commandAdaptor.notifyDataSetChanged();
    }

    private void goSectionList(ItemDto dto) {
        Intent intent = new Intent(this.getApplicationContext(), SectionsActivity.class);
        intent.putExtra(AppConstants.ITEM_KEY, dto);
        startActivity(intent);
    }
}
