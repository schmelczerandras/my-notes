package hu.bme.mynotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.bme.mynotes.MainActivity;
import hu.bme.mynotes.R;
import hu.bme.mynotes.data.Note;
import hu.bme.mynotes.helper.ColorHelper;
import io.noties.markwon.Markwon;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> notes = new ArrayList<>();
    private OpenNoteListener listener;
    private Markwon markwon;

    public NoteAdapter(MainActivity listener) {
        this.listener = listener;
        markwon = Markwon.create(listener);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleView.setText(
                markwon.toMarkdown(note.getTitle())
        );
        holder.note = note;
        holder.drawTags();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void update(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    public interface OpenNoteListener {
        void editNote(Note note);

        void deleteNote(Note note);

        void openNote(Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        Note note;
        private ViewGroup tagContainer;

        NoteViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.noteTitle);
            tagContainer = view.findViewById(R.id.tags);

            view.findViewById(R.id.noteBody).setOnClickListener(v -> listener.openNote(note));
            view.findViewById(R.id.deleteButton).setOnClickListener(v -> listener.deleteNote(note));
            view.findViewById(R.id.editButton).setOnClickListener(v -> listener.editNote(note));
        }

        void drawTags() {
            tagContainer.removeAllViews();
            Context ctx = tagContainer.getContext();

            for (String tag : note.getTags()) {
                View parent = ((LayoutInflater) (
                        Objects.requireNonNull(
                                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                        )
                )).inflate(
                        R.layout.tag, tagContainer, false
                );

                TextView tagView = parent.findViewById(R.id.tag);
                tagView.setText(ColorHelper.formatTag(tag));
                tagContainer.addView(parent);
            }
        }
    }
}
