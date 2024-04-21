package com.example.buisio.Interface;

public interface ClassInteractionListener {
    void onAddClassRequested();
    void onEnrollRequested(String classId,int position);

    void onCancelBookingRequested(String classId, int position);
}
