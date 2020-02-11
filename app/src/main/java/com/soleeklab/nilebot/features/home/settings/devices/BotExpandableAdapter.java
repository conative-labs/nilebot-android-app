package com.soleeklab.nilebot.features.home.settings.devices;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.GetBotsQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.utils.LocalHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotExpandableAdapter extends BaseExpandableListAdapter {


    private OnBotsClicks botsClicks;
    private Context _context;


    private List<GetBotsQuery.Bot> _listDataHeader;
    private List<GetBotsQuery.Bot> filterListDataHeader;


    private HashMap<GetBotsQuery.Bot, List<GetBotsQuery.Sensor>> _listDataChild;


    LocalRepo localRepo;

    public BotExpandableAdapter(Context context, List<GetBotsQuery.Bot> listDataHeader,
                                HashMap<GetBotsQuery.Bot, List<GetBotsQuery.Sensor>> listChildData, OnBotsClicks botClicks) {
        this._context = context;

        this._listDataHeader = listDataHeader;
        this.filterListDataHeader = listDataHeader;

        this._listDataChild = listChildData;
        this.botsClicks = botClicks;
        localRepo = new LocalRepoImpl(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this.filterListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final GetBotsQuery.Sensor sensor = (GetBotsQuery.Sensor) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_bot_child, null);


            convertView.setBackgroundResource(R.color.white);
//            if (LocalHelper.getLanguage().equals("ar"))
            convertView.setPadding(0, 10, 0, 10);
//            else
//                convertView.setPadding(75, 0, 335, 0);


            TextView txtSensorName = convertView.findViewById(R.id.txt_sensor_name);

            View sep = convertView.findViewById(R.id.view_sep);

            txtSensorName.setText(localRepo.getTranslation(sensor.nameID()));


            if (childPosition == (_listDataChild.get(this.filterListDataHeader.get(groupPosition)).size() - 1)) {
                sep.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this.filterListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public int getGroupCount() {
        return this.filterListDataHeader.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return this.filterListDataHeader.get(groupPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GetBotsQuery.Bot bot = (GetBotsQuery.Bot) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_bot_group, null);
            convertView.setPadding(0, 20, 0, 0);
        }

        TextView txtBotName = convertView.findViewById(R.id.txt_bot_name);
        TextView txtSensorNumber = convertView.findViewById(R.id.txt_sensor_number);
        TextView txtBotSoftware = convertView.findViewById(R.id.txt_bot_software);
        TextView txtBotHardware = convertView.findViewById(R.id.txt_bot_hardware);
        ImageView imgNavDown = convertView.findViewById(R.id.img_nav_down);
        ImageView btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete);

        if (LocalHelper.getLanguage().equals("ar")) {
            txtBotName.setGravity(Gravity.RIGHT);
            txtBotName.setText(bot.name() + " (" + bot.botID() + ")");
        } else {
            txtBotName.setText("(" + bot.botID() + ") " + bot.name());
            txtBotName.setGravity(Gravity.LEFT);
        }


        if (!(bot.sw() + "").equals("null"))
            txtBotSoftware.setText(_context.getString(R.string.sw) + "  " + bot.sw());
        else
            txtBotSoftware.setText(_context.getString(R.string.sw) + " 0");

        if (!(bot.hw() + "").equals("null"))
            txtBotHardware.setText(_context.getString(R.string.hw) + "  " + bot.hw());
        else
            txtBotHardware.setText(_context.getString(R.string.hw) + " 0");


        txtSensorNumber.setText(_context.getString(R.string.sensors) + " : " + bot.sensors().size());


        if (bot.sensors().size() > 0) {
            if (isExpanded) {
                imgNavDown.setImageResource(R.drawable.ic_navigate_up);
            } else {
                imgNavDown.setImageResource(R.drawable.ic_navigate_down);
            }

        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botsClicks.delete(bot.botID());
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botsClicks.edit(bot);
            }
        });

//        ExpandableListView mExpandableListView = (ExpandableListView) parent;
//        mExpandableListView.expandGroup(groupPosition);

        return convertView;
    }

    public HashMap<GetBotsQuery.Bot, List<GetBotsQuery.Sensor>> getChildsData() {
        return _listDataChild;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void filterData(String query) {

        query = query.toLowerCase();


        filterListDataHeader = new ArrayList<>();

        if (query.isEmpty()) {
            filterListDataHeader.addAll(_listDataHeader);
        } else {
            for (GetBotsQuery.Bot bot : _listDataHeader) {
                if (bot.name().toLowerCase().contains(query)) {
                    filterListDataHeader.add(bot);
                }
            }
        }
        notifyDataSetChanged();

    }

    public interface OnBotsClicks {

        void delete(String id);

        void edit(GetBotsQuery.Bot bot);

    }
}
