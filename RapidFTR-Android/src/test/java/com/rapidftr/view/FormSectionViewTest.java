package com.rapidftr.view;

import android.view.LayoutInflater;
import com.rapidftr.CustomTestRunner;
import com.rapidftr.R;
import com.rapidftr.activity.RegisterChildActivity;
import com.rapidftr.forms.FormField;
import com.rapidftr.forms.FormSection;
import com.rapidftr.model.Child;
import com.rapidftr.view.fields.*;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(CustomTestRunner.class)
public class FormSectionViewTest {

    private FormSectionView view;
    private FormSection section;
    private FormField field;
    private Child child;

    @Before
    public void setUp() throws JSONException {
        view = (FormSectionView) LayoutInflater.from(new RegisterChildActivity()).inflate(R.layout.form_section, null);
        child = new Child();

        section = new FormSection();
        section.setName(new HashMap<String, String>(){{put("en", "Test Section");}});
        section.setHelpText(new HashMap<String, String>(){{put("en", "Help Section");}});

        view.initialize(section, child);
        field = new FormField();
        field.setId("test_field");
        field.setDisplayName(new HashMap<String, String>(){{put("en", "Test Field");}});
        field.setHelpText(new HashMap<String, String>(){{put("en", "Help Field");}});
        field.setOptionStrings(new HashMap<String, List<String>>(){{put("en",  new ArrayList<String>());}});
    }

    @Test
    public void shouldRenderFormSection() {
        assertThat(view.getLabel().getText().toString(), equalTo(section.getLocalizedName()));
        assertThat(view.getHelpText().getText().toString(), equalTo(section.getLocalizedHelpText()));
        assertThat(view.getContainer().getChildCount(), equalTo(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowTwoFormSectionInitialization() {
        view.initialize(section, child);
        view.initialize(section, child);
    }

    @Test
    public void shouldCreateAudioUploadBox() {
        field.setType("audio_upload_box");
        assertThat(view.createFormField(field), instanceOf(AudioUploadBox.class));
    }

    @Test
    public void shouldCreateCheckBoxes() {
        field.setType("check_boxes");
        assertThat(view.createFormField(field), instanceOf(CheckBoxes.class));
    }

    @Test
    public void shouldCreateDateField() {
        field.setType("date_field");
        assertThat(view.createFormField(field), instanceOf(DateField.class));
    }

    @Test
    public void shouldCreateNumericField() {
        field.setType("numeric_field");
        assertThat(view.createFormField(field), instanceOf(NumericField.class));
    }

    @Test
    public void shouldCreatePhotoUploadBox() {
        field.setType("photo_upload_box");
        assertThat(view.createFormField(field), instanceOf(PhotoUploadBox.class));
    }

    @Test
    public void shouldCreateRadioButtons() {
        field.setType("radio_button");
        assertThat(view.createFormField(field), instanceOf(RadioButtons.class));
    }

    @Test
    public void shouldCreateSelectBox() {
        field.setType("select_box");
        assertThat(view.createFormField(field), instanceOf(SelectBox.class));
    }

    @Test
    public void shouldCreateTextArea() {
        field.setType("textarea");
        assertThat(view.createFormField(field), instanceOf(TextArea.class));
    }

    @Test
    public void shouldCreateTextField() {
        field.setType("text_field");
        assertThat(view.createFormField(field), instanceOf(TextField.class));
    }

    @Test
    public void shouldNotThrowExceptionForUnknownFields() {
        field.setType("abcd");
        section.getFields().add(field);
        assertThat(view.getContainer().getChildCount(), equalTo(0));
    }

    @Test
    public void shouldRenderMultipleFields() {
        FormField field1 = new FormField(), field2 = new FormField(), field3 = new FormField();
        field1.setId("text_field");
        field2.setId("abcd");
        field3.setId("textarea");

        field1.setType("text_field");
        field2.setType("abcd");
        field3.setType("textarea");

        view = spy(view);
        section.getFields().addAll(Arrays.asList(field1, field2, field3));
        view.initialize();

        assertThat(view.getContainer().getChildCount(), equalTo(2));
        verify(view).createFormField(field1);
        verify(view).createFormField(field2);
        verify(view).createFormField(field3);
    }

}
