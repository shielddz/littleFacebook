package com.usthb.modeles;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServeurMiniFaceBook {
    static private HashMap<String, Abonne> baseAbonnes = new HashMap<>();

    private static boolean connecter(ObjectOutputStream out, String[] tokens) throws IOException {
        String username = tokens[1];
        String password = tokens[2];
        if(baseAbonnes.containsKey(username)){
            if(baseAbonnes.get(username).comparePassword(password)){
                baseAbonnes.get(username).online = true;
                out.writeObject(baseAbonnes.get(username));
                out.flush();
                return true;
            }
        }
        out.writeObject(null);
        out.flush();
        return false;
    }
    private static boolean update(ObjectOutputStream out, String[] tokens) throws IOException {
        String username = tokens[1];
        if(baseAbonnes.containsKey(username)){
            out.reset();
            out.writeObject(baseAbonnes.get(username));
            out.flush();
            return true;
        }
        out.writeObject(null);
        out.flush();
        return false;
    }
    private static boolean inscrireAbonne(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        Abonne abonne = (Abonne)in.readObject();

        if(baseAbonnes.containsKey(abonne.getUsername())){
            out.writeBoolean(false);
            out.flush();
            return false;
        }
        baseAbonnes.put(abonne.getUsername(), abonne);
        out.writeBoolean(true);
        out.flush();
        return true;
    }
    private static boolean ajouterInvitationAmi(ObjectInputStream in, String[] tokens) throws IOException, ClassNotFoundException {
        Invitation invitation = (Invitation) in.readObject();
        Abonne abonne1 = baseAbonnes.get(tokens[1]);
        Abonne abonne2 = baseAbonnes.get(invitation.getUser());
        if(abonne1.aAmi(abonne2.getUsername())){
            return false;
        }
        if(abonne1.aInvitation(abonne2.getUsername())){
            if(abonne1.getInvitation(abonne2.getUsername()).getEtat() != EtatInvitation.refusee){
                return false;
            }
        }
        invitation.nom = abonne2.nom;
        invitation.prenom = abonne2.prenom;
        abonne1.getInvitations().add(invitation);
        return true;
    }
    private static boolean modifierInvitationAmi(String[] tokens){
        Abonne abonne1 = baseAbonnes.get(tokens[1]);
        Abonne abonne2 = baseAbonnes.get(tokens[2]);
        EtatInvitation nouvelEtat = EtatInvitation.valueOf(tokens[3]);
        if(abonne1.aInvitation(abonne2.getUsername())){
            if(nouvelEtat == EtatInvitation.acceptee){
                abonne1.getAmis().add(abonne2);
                abonne2.getAmis().add(abonne1);
            }
            abonne1.getInvitation(abonne2.getUsername()).setEtat(nouvelEtat);
            return true;
        }
        return false;
    }
    private static boolean supprimerAbonne(String[] tokens) {
        Abonne abonne1 = baseAbonnes.get(tokens[1]);
        Abonne abonne2 = baseAbonnes.get(tokens[2]);

        if(abonne1.aAmi(abonne2.getUsername())){
            abonne1.getAmis().remove(abonne2);
            abonne2.getAmis().remove(abonne1);
            return true;
        }
        return false;

    }
    private static boolean rechercher(ObjectOutputStream out, String[] tokens) throws IOException {
        String username = tokens[1];
        if(baseAbonnes.containsKey(username)){
            out.writeObject(baseAbonnes.get(username));
            out.flush();
            return true;
        }
        out.writeObject(null);
        out.flush();
        return false;
    }
    private static boolean deconnecter(ObjectOutputStream out, String[] tokens) throws IOException {
        String username = tokens[1];
        if(baseAbonnes.containsKey(username)){
            if(baseAbonnes.get(username).online){
                baseAbonnes.get(username).online = false;
                out.writeBoolean(true);
                out.flush();
                return true;
            }
        }
        out.writeBoolean(false);
        out.flush();
        return false;
    }
    private static boolean publier(ObjectInputStream in, ObjectOutputStream out, String[] tokens) throws IOException, ClassNotFoundException {
        Publication publicationRecue = (Publication)in.readObject();
        Publication publication = new Publication(publicationRecue);
        String username = tokens[1];

        Abonne abonne = baseAbonnes.get(username);

        if(!publication.getContenu().isEmpty()){
            abonne.getPublications().add(publication);
            if(publication.getNiveau() != niveauVisibilitePublication.prive) {
                for (Abonne abo : abonne.getAmis()) {
                    abo.getNotifications().add(new Notification(new StringBuilder(abonne.nom + " " + abonne.prenom + " a publier."), TypeNotification.publication));
                }
            }
            return true;
        }
        return false;
    }
    private static boolean commenter(ObjectInputStream in, String[] tokens) throws IOException, ClassNotFoundException {
        int id = Integer.parseInt(tokens[2]);
        Commentaire commentaire = (Commentaire) in.readObject();
        Abonne abonne = baseAbonnes.get(tokens[1]);
        if(commentaire.getCommentaire().isEmpty()){
            return false;
        }
        Publication publication = abonne.publicationParId(id);
        if(publication == null){
            return false;
        }
        //abo.getNotifications().add(new Notification(new StringBuilder(abonne.nom + " " + abonne.prenom + " a publier."), TypeNotification.publication)
        abonne.getNotifications().add(new Notification(new StringBuilder(commentaire.user.nom+" "+commentaire.user.prenom+" a commenté votre publication."), TypeNotification.commentaire));
        publication.getCommentaires().add(commentaire);
        return true;
    }
    private static boolean epinglee(String[] tokens)  {
        Abonne abonne = baseAbonnes.get(tokens[1]);
        int id = Integer.parseInt(tokens[2]);
        Publication publication = abonne.publicationParId(id);
        if(!publication.isEpinglee()){
            publication.setEpinglee(true);
            return true;
        }
        return false;
    }
    private static boolean desepinglee(String[] tokens) {
        Abonne abonne = baseAbonnes.get(tokens[1]);
        int id = Integer.parseInt(tokens[2]);
        Publication publication = abonne.publicationParId(id);
        if(publication.isEpinglee()){
            publication.setEpinglee(false);
            return true;
        }
        return false;
    }
    private static boolean reagir(ObjectInputStream in, ObjectOutputStream out, String[] tokens) throws IOException, ClassNotFoundException {
        int id = Integer.parseInt(tokens[3]);
        Reaction reaction = (Reaction) in.readObject();

        Abonne abonne = baseAbonnes.get(tokens[1]);//publication owner
        Abonne abonneR = baseAbonnes.get(tokens[2]);//the one that reacts to the publication
        Publication publication = abonne.publicationParId(id);
        publication.supprimerReactionUsername(publication.usernameReaction(abonneR.getUsername()));
        publication.reactions.add(reaction);
        if(!abonne.equals(abonneR)){
            abonne.getNotifications().add(new Notification(new StringBuilder(abonneR.nom + " " + abonneR.prenom + " a réagi a votre publication."), TypeNotification.reaction));
        }
        return true;
    }
    private static boolean partager(ObjectInputStream in, ObjectOutputStream out, String[] tokens) throws IOException, ClassNotFoundException {
        Publication publication = (Publication) in.readObject();

        Abonne abonne = baseAbonnes.get(tokens[1]);//the one that shares
        Abonne abonnePere = baseAbonnes.get(tokens[2]);//the owner ( Parent )
        int idPublicationMere = Integer.parseInt(tokens[3]);
        niveauVisibilitePublication niveau = niveauVisibilitePublication.valueOf(tokens[4]);
        Publication publicationMere = abonnePere.publicationParId(idPublicationMere);
        publicationMere.nombrePartages++;
        if(true) {
            Publication nouvellePublication = new Publication(publication.getContenu(), niveau, false, abonnePere);
            abonne.getPublications().add(nouvellePublication);
            for (Abonne abo : abonne.getAmis()) {
                abo.getNotifications().add(new Notification(new StringBuilder(abonne.nom + " " + abonne.prenom + " a partagé."), TypeNotification.publication));
            }
            return true;
        }
        return false;

    }
    private static boolean setNotificationsSeen(String[] tokens) {
        Abonne abonne = baseAbonnes.get(tokens[1]);
        for (Notification notif:abonne.getNotifications()) {
            notif.setLue(true);
        }
        return true;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //hardcoded Part
        Abonne abonne1 = new Abonne("Yamoun", "Ilyes", "ishielddz", "pass", "ISIL", "etudiant", "L2", new MaDate("10/06/1998"), Categorie.homme);
        Abonne abonne2 = new Abonne("lNameA", "fNameA", "a", "a", "ISILa", "etudianta", "L2", new MaDate("10/06/1998"), Categorie.homme);
        Abonne abonne3 = new Abonne("lNameB", "fNameB", "b", "b", "ISILb", "etudiantb", "L2", new MaDate("10/06/1998"), Categorie.femme);
        Abonne abonne4 = new Abonne("lNameC", "fNameC", "c", "c", "ISILc", "etudiantc", "L2", new MaDate("10/06/1998"), Categorie.homme);
        baseAbonnes.put("ishielddz", abonne1);
        baseAbonnes.put("a", abonne2);
        baseAbonnes.put("b", abonne3);
        baseAbonnes.put("c", abonne4);

        ServerSocket server = new ServerSocket(1337);

        while(true){
            System.out.println("Waiting for a user to connect..");
            Socket clientSocket = server.accept();
            System.out.println("user connected");

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            boolean check = false, quit=false;

            while(!quit){
                //the request is sometimes like : connect username password
                //so it's got to be split up
                //it's just an optimization
                //so that the server doesn't have to exchange a lot of information with the client
                String request = (String) in.readObject();
                String[] tokens = request.split(" ");
                if (tokens.length > 0) {
                    String cmd = tokens[0];
                    switch (cmd) {
                        case "connect":
                            check = connecter(out, tokens);
                            break;
                        case "signup":
                            check = inscrireAbonne(in, out);
                            break;
                        case "delete":
                            check = supprimerAbonne(tokens);
                            break;
                        case "search":
                            check = rechercher(out, tokens);
                            break;
                        case "disconnect":
                            check = deconnecter(out, tokens);
                            break;
                        case "post":
                            check = publier(in, out, tokens);
                            break;
                        case "pin":
                            check = epinglee(tokens);
                            break;
                        case "unpin":
                            check = desepinglee(tokens);
                            break;
                        case "share":
                            check = partager(in, out, tokens);
                            break;
                        case "reaction":
                            check = reagir(in, out, tokens);
                            break;
                        case "invitation":
                            check = ajouterInvitationAmi(in, tokens);
                            break;
                        case "modifyInvitation":
                            check = modifierInvitationAmi(tokens);
                            break;
                        case "seenNotifications":
                            check = setNotificationsSeen(tokens);
                            break;
                        case "comment":
                            check = commenter(in, tokens);
                            break;
                        case "update":
                            check = update(out, tokens);
                            break;
                        case "quit":
                            check = true;
                            quit = true;
                            break;
                        default:
                            out.writeObject("Error.");
                            out.flush();
                            check = false;
                            quit = true;
                    }
                }
                System.out.print(tokens[0]);
                System.out.println((check ? ":Success" : ":Error."));
            }
            clientSocket.close();
        }
    }
}
