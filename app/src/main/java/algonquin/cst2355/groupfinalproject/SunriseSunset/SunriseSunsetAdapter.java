package algonquin.cst2355.groupfinalproject.SunriseSunset;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2355.groupfinalproject.R;

public class SunriseSunsetAdapter extends RecyclerView.Adapter<SunriseSunsetAdapter.ViewHolder> {

    private SunriseSunsetMainActivity activity;
    private List<LocationItem> data;
    private int selectedPosition = -1;
    private ItemClickListener listener;

    public SunriseSunsetAdapter(SunriseSunsetMainActivity activity, List<LocationItem> data) {
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationItem item = data.get(position);
        holder.textViewLocation.setText(item.getLatitude() + ", " + item.getLongitude());
        holder.itemView.setOnClickListener(holder);
        holder.buttonDelete.setOnClickListener(holder);
        holder.buttonDelete.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewLocation;
        Button buttonDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.buttonDelete) {
                listener.onDeleteClick(position);
            } else {
                listener.onItemClick(v, position);
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onDeleteClick(int position);
    }
}

