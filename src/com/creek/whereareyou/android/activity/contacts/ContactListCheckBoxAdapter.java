package com.creek.whereareyou.android.activity.contacts;

import java.util.List;

import com.creek.whereareyou.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ContactListCheckBoxAdapter extends ArrayAdapter<CheckBoxContact> {

    private final List<CheckBoxContact> list;
    private final Activity context;

    public ContactListCheckBoxAdapter(Activity context, List<CheckBoxContact> list) {
        super(context, R.layout.add_contact_row, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.add_contact_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.contact_name);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.contact_check);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBoxContact element = (CheckBoxContact) viewHolder.checkbox.getTag();
                    element.setSelected(buttonView.isChecked());

                }
            });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getContact().getDisplayName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }
}
