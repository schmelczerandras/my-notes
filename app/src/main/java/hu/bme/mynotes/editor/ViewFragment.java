package hu.bme.mynotes.editor;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import hu.bme.mynotes.R;
import hu.bme.mynotes.helper.ColorHelper;
import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.image.ImagesPlugin;


public class ViewFragment extends Fragment {
    private String text;
    private Markwon markwon;
    private TextView textContainer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        markwon = Markwon.builder(container.getContext())
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
                        builder.linkColor(ColorHelper.getBrightColor())
                                .blockQuoteColor(ColorHelper.getBrightColor())
                                .listItemColor(ColorHelper.getBrightColor())
                                .headingBreakHeight(0);

                    }
                })
                .usePlugin(ImagesPlugin.create())
                .usePlugin(TaskListPlugin.create(container.getContext()))
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TablePlugin.create(container.getContext()))
                .build();

        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textContainer = view.findViewById(R.id.textContainer);
        setText(text);
    }

    void setText(String text) {
        this.text = text;
        if (textContainer != null && text != null) {
            markwon.setMarkdown(textContainer, text);
        }
    }
}
