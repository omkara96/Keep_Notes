package com.omkar.varma.keepprivate.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.omkar.varma.keepprivate.R;
import com.omkar.varma.keepprivate.adapters.NotesAdapter;
import com.omkar.varma.keepprivate.database.NotesDatabase;
import com.omkar.varma.keepprivate.entities.Note;
import com.omkar.varma.keepprivate.listeners.NotesListeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListeners, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;

    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private  int noteClickedPosition =-1;

    private AlertDialog dialogAddURL;
    private AlertDialog dialogSendApp;

    /*
    Drawer Layout Variables;
     */
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNotesMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList,this);
        notesRecyclerView.setAdapter(notesAdapter);
        getNotes(REQUEST_CODE_SHOW_NOTES,false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(noteList.size() !=0){
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });


        /*
        Quick Action Functions directly implemented by onclick
         */

        findViewById(R.id.imageAddNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        findViewById(R.id.imageAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                }else {
                    selectImage();
                }
            }
        });

        findViewById(R.id.imageAddWebLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddURLDialog();
            }
        });




        /*
        Drawer layout Functions
         */
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.navigration_open,R.string.navigration_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigationDrawer_Home);

         /*
        Hide or Show Navigation Menu Items
         */
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.navigationDrawer_RateUs).setVisible(false);

        /*
        menu implimentation + secret long click
         */
        ImageView menuImageView = findViewById(R.id.menuImage);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        menuImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "System is Redirecting...", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    private void selectImage(){
            /*Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select image"),REQUEST_CODE_SELECT_IMAGE);*/
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else {
                Toast.makeText(this,"Permission Denied !!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath;

        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = getContentResolver()
                    .query(contentUri, null, null, null);
        }else{
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = managedQuery(contentUri, projection, null, null, null);
            startManagingCursor(cursor);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        if(cursor == null){
            filePath = contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted){
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
               if(requestCode == REQUEST_CODE_SHOW_NOTES){
                   noteList.addAll(notes);
                   notesAdapter.notifyDataSetChanged();
               }else if(requestCode == REQUEST_CODE_ADD_NOTE){
                   noteList.add(0,notes.get(0));
                   notesAdapter.notifyItemInserted(0);
                   notesRecyclerView.smoothScrollToPosition(0);
               }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                   noteList.remove(noteClickedPosition);
                   if(isNoteDeleted){
                       notesAdapter.notifyItemRemoved(noteClickedPosition);
                   }else {
                       noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                       notesAdapter.notifyItemChanged(noteClickedPosition);
                   }
               }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_NOTE &&  resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE,false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }else if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data !=null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try{
                        String selectedImagePath = getPathFromUri(selectedImageUri);
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("QuickActionType","image");
                        intent.putExtra("imagePath",selectedImagePath);
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                    }catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showAddURLDialog(){
        if(dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );

            builder.setView(view);
            dialogAddURL = builder.create();
            if(dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputURL.getText().toString().trim().isEmpty()){
                        Toast.makeText(MainActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    }else  if(!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                        Toast.makeText(MainActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                    }else {
                        dialogAddURL.dismiss();
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("QuickActionType","URL");
                        intent.putExtra("URL",inputURL.getText().toString());
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                    }
                }
            });

            view.findViewById(R.id.textcancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }
        dialogAddURL.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigationDrawer_Home:
                onBackPressed();
                break;

            case R.id.navigationDrawer_AddNote:
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
                break;

            case R.id.navigationDrawer_QuickIntro:
                startActivity(new Intent(this,On_Boarding_Screen_Intro.class));
                break;

            case R.id.navigationDrawer_AboutUs:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;

            case R.id.navigationDrawer_Help_Feedback:
                startActivity(new Intent(this,HelpFeedbackActivity.class));
                break;

            case R.id.navigationDrawer_Share:
               showDialogSendApp();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogSendApp(){
        if(dialogSendApp == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.share_app_background,
                    (ViewGroup) findViewById(R.id.layoutShareAppContainer)
            );

            builder.setView(view);
            dialogSendApp = builder.create();
            if(dialogSendApp.getWindow() != null){
                dialogSendApp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }


            view.findViewById(R.id.textShareViaLink).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSendApp.dismiss();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_SUBJECT,"App Link");
                    share.putExtra(Intent.EXTRA_TEXT,"www.google.com");
                    startActivity(Intent.createChooser(share,"Share link!"));

                }
            });

            view.findViewById(R.id.textShareApk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSendApp.dismiss();
                    sendAPP();
                }
            });

            view.findViewById(R.id.textShareCancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSendApp.dismiss();
                }
            });
        }
        dialogSendApp.show();
    }

    private void sendAPP(){
        ApplicationInfo api = getApplicationContext().getApplicationInfo();
        String apkPath = api.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
        startActivity(Intent.createChooser(intent, "Share Via."));
    }
}