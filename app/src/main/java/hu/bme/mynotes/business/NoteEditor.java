package hu.bme.mynotes.business;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.bme.mynotes.adapter.NoteAdapter;
import hu.bme.mynotes.data.Note;
import hu.bme.mynotes.data.NoteDao;

public class NoteEditor {
    private static NoteEditor instance = null;

    private List<Note> notes;
    private Set<String> selectedTags;
    private Set<String> tags;

    private OnTagsChanged listener;

    private NoteDao dao;
    private NoteAdapter adapter;

    private Note editedNote;

    private NoteEditor(NoteDao dao, NoteAdapter adapter, OnTagsChanged listener) {
        this.dao = dao;
        this.adapter = adapter;
        this.listener = listener;
        this.selectedTags = new HashSet<>();
    }

    public static void initialize(NoteDao dao, NoteAdapter adapter, OnTagsChanged listener) {
        instance = new NoteEditor(dao, adapter, listener);
    }

    public static NoteEditor getInstance() {
        if (instance == null) {
            throw new RuntimeException("Uninitialized.");
        }

        return instance;
    }

    public void startEditing(@Nullable Note note) {
        if (note == null) {
            note = new Note();
            onNoteCreated(note);
        }
        editedNote = note;
    }

    public Note getEditedNote() {
        return editedNote;
    }


    public Set<String> getSelectedTags() {
        return selectedTags;
    }

    public List<Note> getNotes() {
        return notes;
    }

    private void setNotes(List<Note> notes) {
        this.tags = new HashSet<>();

        for (Note n : notes) {
            this.tags.addAll(n.getTags());
        }
        Collections.sort(notes, (n1, n2) -> n1.getTitle().compareToIgnoreCase(n2.getTitle()));

        this.notes = notes;

        listener.onTagsChanged(tags);

        showNotes();
    }

    public void addSelectedTag(String selectedTag) {
        selectedTags.add(selectedTag);
        showNotes();
    }

    public void removeSelectedTag(String selectedTag) {
        selectedTags.remove(selectedTag);
        showNotes();
    }

    private void showNotes() {
        List<Note> shownNotes = new ArrayList<>();
        for (Note n : notes) {
            for (String tag : n.getTags()) {
                if (selectedTags.contains(tag)) {
                    shownNotes.add(n);
                    break;
                }
            }
        }
        adapter.update(shownNotes);
    }


    public void saveEdited() {
        onNoteChanged(editedNote);
    }

    @SuppressLint("StaticFieldLeak")
    public void loadNotesInBackground() {
        new AsyncTask<Void, Void, List<Note>>() {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return dao.getAll();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                setNotes(notes);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void onNoteChanged(final Note note) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                dao.update(note);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                loadNotesInBackground();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void onNoteCreated(final Note note) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                note.id = null;
                note.id = dao.insert(note);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                loadNotesInBackground();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void onNoteDeleted(final Note note) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                dao.delete(note);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                loadNotesInBackground();
            }
        }.execute();
    }

    public interface OnTagsChanged {
        void onTagsChanged(Set<String> tags);
    }
}
