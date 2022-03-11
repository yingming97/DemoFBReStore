package net.fpl.demofbrestore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterUser extends BaseAdapter {
    ArrayList<User> list;

    public AdapterUser(ArrayList<User> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public User getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.item_user, null);
        } else {
            view = convertView;
        }
        TextView tvUser = view.findViewById(R.id.tv_user);
        TextView tvPass = view.findViewById(R.id.tv_pass);
        User user = this.list.get(position);
        tvUser.setText("Name: "+user.getName());
        tvPass.setText("Pass: "+user.getPass());

        return view;
    }
}
