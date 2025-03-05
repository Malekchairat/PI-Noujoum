package models;

import java.util.Date;
import java.util.Objects;

public class Feedback {
    private int id;
    private Reclamation rec;
    private User user;
    private int note; // 1-5 rating
    private String commentaire;
    private Date dateFeedback;

    public Feedback(int id, Reclamation rec, User user, int note, String commentaire, Date dateFeedback) {
        this.id = id;
        this.rec = rec;
        this.user = user;
        this.note = note;
        this.commentaire = commentaire;
        this.dateFeedback = dateFeedback;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reclamation getRec() {
        return rec;
    }

    public void setRec(Reclamation rec) {
        this.rec = rec;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateFeedback() {
        return dateFeedback;
    }

    public void setDateFeedback(Date dateFeedback) {
        this.dateFeedback = dateFeedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return id == feedback.id && note == feedback.note && Objects.equals(rec, feedback.rec) && Objects.equals(user, feedback.user) && Objects.equals(commentaire, feedback.commentaire) && Objects.equals(dateFeedback, feedback.dateFeedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rec, user, note, commentaire, dateFeedback);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", rec=" + rec +
                ", user=" + user +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                ", dateFeedback=" + dateFeedback +
                '}';
    }
}
