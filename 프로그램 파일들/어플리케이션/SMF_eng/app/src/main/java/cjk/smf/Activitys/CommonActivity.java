package cjk.smf.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cjk.smf.MainActivity;
import cjk.smf.R;


public class CommonActivity extends AppCompatActivity {
    public SharedPreferences settings;
    String name = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 스택 중간 액티비티 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티가 이미 있으면 재사용
                startActivity(intent);

                //finish();

                return (true);

//            case R.id.home:
//
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 스택 중간 액티비티 제거
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티가 이미 있으면 재사용
//                startActivity(intent);
//
//                //finish();
//
//                return (true);

            case R.id.about:
                settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
                name = settings.getString("userNM", "");
                Toast.makeText(getApplicationContext(),
                        "Login with " + name,
                        Toast.LENGTH_SHORT)
                        .show();

                return (true);

            case R.id.logout:

                settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear(); // 모든 정보 삭제
                editor.apply();

                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 스택 중간 액티비티 제거
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티가 이미 있으면 재사용
                startActivity(intent2);

                finish();
                return (true);

        }

        return super.onOptionsItemSelected(item);
    }
}
