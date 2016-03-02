package pira.stellerin.com.projectinvite.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pira.stellerin.com.projectinvite.Entity.Programme;
import pira.stellerin.com.projectinvite.R;


public class ProgrammeAdapter extends ArrayAdapter<Programme> {

	Context context;
	int layoutResourceId;
	List<Programme> programme = null;
	Boolean visible;
	public ProgrammeAdapter(Context context, int layoutResourceId,
							List<Programme> programme, Boolean visible) {
		super(context, layoutResourceId, programme);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.programme = programme;
		this.visible = visible;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new UserHolder();
			holder.txtName = (TextView) row.findViewById(R.id.titre_programme);
			holder.txtDate = (TextView) row.findViewById(R.id.textdate);
			holder.txtDes = (TextView) row.findViewById(R.id.textdescription);
			holder.getTxtDes = (TextView) row.findViewById(R.id.textdes);
			holder.image = (ProfilePictureView) row
					.findViewById(R.id.img_programme_small);
			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}

		Programme prog = programme.get(position);
		
		final DateFormat FORMATTING_PATTERN = new SimpleDateFormat(
				"yyyy-MM-dd");
		String outputDate = "";
		outputDate = FORMATTING_PATTERN.format(prog.getDate()).toString();
		ParseUser user = prog.getUser();
		JSONObject jsonobject = user.getJSONObject("profile");
		holder.image.setProfileId(jsonobject.optString("facebookId"));
		holder.txtDate.setText(outputDate);
		holder.txtDes.setText(prog.getPlace());
		holder.txtName.setText(prog.getTitre());
		holder.getTxtDes.setText(prog.getDescription());
		return row;
	}

	static class UserHolder {
		TextView txtDes;
		TextView txtDate;
		TextView txtName;
		TextView getTxtDes;
		ProfilePictureView image;
	}
}
