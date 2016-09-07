package example.com.wordslayoutmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.WordsLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TestAdapter adapter;
    Random      rnd;
    String      lorem;
    String[]    loremArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rnd = new Random();

        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fillList();
            }
        });

        lorem = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas porttitor congue massa. Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna. Nunc viverra imperdiet enim. Fusce est. Vivamus a tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin pharetra nonummy pede";
        lorem = lorem.replaceAll("\\,", "");
        lorem = lorem.replaceAll("\\.", "");
        loremArr = lorem.split(" ");

        fillList();
    }

    private void fillList() {
        List<TestModel> testArray = new ArrayList<>();
        for (String word : loremArr) {
            testArray.add(new TestModel(word));
        }
//        for (int i = 0; i < 150; i++) {
//            testArray.add(new TestModel(""+i));
//        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new WordsLayoutManager(this));
        adapter = new TestAdapter(testArray);
        recycler.setAdapter(adapter);
    }

    public static class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
        public List<TestModel> models;

        public TestAdapter(List<TestModel> models) {
            this.models = models;
        }

        @Override
        public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.row_layout, parent, false);

            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(TestAdapter.ViewHolder viewHolder, int position) {
            TestModel model = models.get(position);
            viewHolder.title.setText(model.text);
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }

    public class TestModel {
        public String text;

        public TestModel() {
            text = "";
            for (int ii = 0; ii < (1 + rnd.nextInt(10)); ii++) {
                text += "XX";
            }
        }

        public TestModel(String word) {
            text = word;
        }

    }
}


