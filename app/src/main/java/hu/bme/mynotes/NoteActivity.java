package hu.bme.mynotes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hu.bme.mynotes.business.NoteEditor;
import hu.bme.mynotes.editor.SectionsPagerAdapter;

import static hu.bme.mynotes.MainActivity.NOTE_KEY;
import static hu.bme.mynotes.editor.SectionsPagerAdapter.VIEW_PAGE;


public class NoteActivity extends AppCompatActivity {
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                sectionsPagerAdapter.setTextForView(sectionsPagerAdapter.getTextOfEditor());
            }
        });
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        String startingScreen = getIntent().getStringExtra(NOTE_KEY);
        sectionsPagerAdapter.setTextForEditor(NoteEditor.getInstance().getEditedNote().content);
        if (startingScreen != null) {
            viewPager.setCurrentItem(VIEW_PAGE);
        }
    }

    @Override
    protected void onPause() {
        saveChanges();
        super.onPause();
    }

    private void saveChanges() {
        NoteEditor.getInstance().getEditedNote().content = sectionsPagerAdapter.getTextOfEditor();
        NoteEditor.getInstance().saveEdited();
    }
}
