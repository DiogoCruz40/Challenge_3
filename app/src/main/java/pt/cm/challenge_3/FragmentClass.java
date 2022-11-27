package pt.cm.challenge_3;

import androidx.fragment.app.Fragment;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.Interfaces.FragmentInterface;


public class FragmentClass extends Fragment implements FragmentInterface {

    private SharedViewModel mViewModel;
    //private ListAdapter adapter;
    private ActivityInterface activityInterface;

    public FragmentClass() {

    }

    /*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }*/

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        setHasOptionsMenu(true);

        this.mViewModel = new ViewModelProvider(activityInterface.getmainactivity()).get(SharedViewModel.class);
        mViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notes);
            adapter = new ListAdapter(notes, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
        });

        return view;
    }*/

    /*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_one, menu);
        MenuItem menuItem = menu.findItem(R.id.searchbar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<NoteDTO> notes = adapter.getNotes();
                if (s.length() != 0) {
                    List<NoteDTO> filteredList = new ArrayList<NoteDTO>();
                    for (NoteDTO n : notes) {
                        if (n.getTitle().toLowerCase().contains(s.toLowerCase())) {
                            filteredList.add(n);
                        }
                    }
                    if (filteredList.isEmpty()) {
                        Toast.makeText(activityInterface.getmainactivity(), "No Results for your search", Toast.LENGTH_LONG).show();
                    } else {
                        adapter.setFilteredNotes(filteredList);
                    }
                } else {
                    adapter.setFilteredNotes(notes);
                }

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    */

    /*
    public void mqttPopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getmainactivity());
        final View mqttPopUp = getLayoutInflater().inflate(R.layout.mqtt_popup, null);
        cancel = (Button) mqttPopUp.findViewById(R.id.cancelbuttonmqtt);
        subscribe = (Button) mqttPopUp.findViewById(R.id.subscribebuttonmqtt);
        unsubscribe = (Button) mqttPopUp.findViewById(R.id.unsubbuttonmqtt);
        spinnermqttpopup = (Spinner) mqttPopUp.findViewById(R.id.spinnermqtt);

        mViewModel.getTopics().observe(activityInterface.getmainactivity(), topics -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getmainactivity(), android.R.layout.simple_spinner_item, topics);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnermqttpopup.setAdapter(adapter);
        });
        dialogBuilder.setView(mqttPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribetopic = (EditText) mqttPopUp.findViewById(R.id.subscribemqtt);
                if (subscribetopic.getText().toString().isBlank())
                    Toast.makeText(activityInterface.getmainactivity(), "Write a topic", Toast.LENGTH_SHORT).show();
                else if (mViewModel.subscribeToTopic(subscribetopic.getText().toString()))
                    subscribetopic.setText("");
                else
                    Toast.makeText(activityInterface.getmainactivity(), "Already subscribed", Toast.LENGTH_SHORT).show();
            }
        });

        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnermqttpopup.getSelectedItem() != null)
                    mViewModel.unsubscribeToTopic(spinnermqttpopup.getSelectedItem().toString());
                else
                    Toast.makeText(activityInterface.getmainactivity(), "Nothing to unsubscribe", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }*/

    /*
    public void mqttMsgPopUp(String topic, MqttMessage message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getmainactivity());
        final View mqttmesagePopUp = getLayoutInflater().inflate(R.layout.mqtt_message_popup, null);
        Button confirm = (Button) mqttmesagePopUp.findViewById(R.id.confirmmsgbtnmqtt);
        Button cancel = (Button) mqttmesagePopUp.findViewById(R.id.cancelmsgbtnmqtt2);
        TextView topico = (TextView) mqttmesagePopUp.findViewById(R.id.topicmsgmqtt);
        TextView titulo = (TextView) mqttmesagePopUp.findViewById(R.id.titlemsgmqtt);
        NoteDTO noteDTO = new Gson().fromJson(message.toString(), NoteDTO.class);
        topico.setText(topic);
        titulo.setText(noteDTO.getTitle());

        dialogBuilder.setView(mqttmesagePopUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.insertMqttNote(noteDTO.getTitle(), noteDTO.getDescription());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }*/

}
