/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sportsclubmanagementsystem;

import com.mycompany.sportsclubmanagementsystem.Entity.Match;
import com.mycompany.sportsclubmanagementsystem.Entity.Team;
import com.mycompany.sportsclubmanagementsystem.Entity.Tournament;
import com.mycompany.sportsclubmanagementsystem.Entity.Venue;
import static com.mycompany.sportsclubmanagementsystem.LoginCredentials.password;
import static com.mycompany.sportsclubmanagementsystem.LoginCredentials.username;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author DELL
 */
public class EditMatches extends javax.swing.JFrame {

    public ArrayList<Tournament> TournamentList = new ArrayList();
    public ArrayList<Match> MatchList = new ArrayList();
    public ArrayList<Venue> VenueList = new ArrayList();
    public ArrayList<Team> TeamList_1 = new ArrayList();
    public ArrayList<Team> TeamList_2 = new ArrayList();
    int oldTeam1Id = 0;
    int oldTeam2Id = 0;
    int oldVenueId = 0;

    /**
     * Creates new form EditMatches
     */
    public EditMatches() {
        initComponents();
        TournamentCBFillData();
        MatchCBFillData();
        FillOldTeamsData();
        FillOldVenueData();
        Team_1CBFillData();
        Team_2CBFillData();
        VenueCBFillData();
    }

    //to show Tournament data in combo box
    private void TournamentCBFillData() {
        try {
            System.out.println("--------------1");

            Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", username, password);
            String selectQuery = "select * from TOURNAMENT";
            PreparedStatement mystmt = mycon.prepareStatement(selectQuery);
            ResultSet results = mystmt.executeQuery();
            while (results.next()) {
                Tournament tou = new Tournament(results.getInt("TOU_id"), results.getString("TOU_name"));
                TournamentList.add(tou);
                System.out.println(results.getString("TOU_name"));
                jComboBox6.addItem(results.getString("TOU_name"));

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //to show match data in combo box
    private void MatchCBFillData() {
        try {
            System.out.println("--------------2");

            String touName = jComboBox6.getItemAt(0);

            int touId = TournamentList.get(0).id;
            for (int i = 0; i < TournamentList.size(); i++) {
                Tournament t = TournamentList.get(i);
                if (t.name.equals(touName)) {
                    touId = t.id;
                }
            }
            

            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            PreparedStatement stmt = con.prepareStatement("select * from MATCHES where tournament_tou_id =? ");
            stmt.setInt(1, touId);
            ResultSet rst = stmt.executeQuery();

            while (rst.next()) {
                Match m = new Match(rst.getInt("m_id"), rst.getString("m_name"));
                MatchList.add(m);
                jComboBox2.addItem(rst.getString("m_name"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //fil old team1 and team2 data based on zero index match selected
    private void FillOldTeamsData() {
        try {
            System.out.println("--------------3");

            String matchName = jComboBox2.getItemAt(0);

            int matchId = MatchList.get(0).id;
            for (int i = 0; i < MatchList.size(); i++) {
                Match t = MatchList.get(i);
                if (t.name.equals(matchName)) {
                    matchId = t.id;
                }
            }

            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            PreparedStatement stmt = con.prepareStatement("select t.te_id\n"
                    + "from team t\n"
                    + "inner join team_mat tm on tm.team_te_id = t.te_id\n"
                    + "where tm.matches_m_id = ?");
            stmt.setInt(1, matchId);
            ResultSet rst = stmt.executeQuery();

            int i = 0;
            while (rst.next()) {
                if (i == 0) {
                    oldTeam1Id = rst.getInt("te_id");
                    i++;
                } else {
                    oldTeam2Id = rst.getInt("te_id");
                }
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //fil old venue data based on zero index match selected
    private void FillOldVenueData() {
        try {
            System.out.println("--------------4");

            String matchName = jComboBox2.getItemAt(0);

            int matchId = MatchList.get(0).id;
            for (int i = 0; i < MatchList.size(); i++) {
                Match t = MatchList.get(i);
                if (t.name.equals(matchName)) {
                    matchId = t.id;
                }
            }

            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            PreparedStatement stmt = con.prepareStatement("select v.v_id \n"
                    + "from venue v\n"
                    + "inner join matches m on m.venue_v_id = v.v_id\n"
                    + "where m.m_id = ?");
            stmt.setInt(1, matchId);
            ResultSet rst = stmt.executeQuery();

            while (rst.next()) {
                oldVenueId = rst.getInt("v_id");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //to show data in team1 combo box
    private void Team_1CBFillData() {
        try {
            System.out.println("--------------5");
            Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            Statement mystmt = mycon.createStatement();
            String selectQuery = "select * from Team";
            ResultSet results = mystmt.executeQuery(selectQuery);
            while (results.next()) {
                Team team_2 = new Team(results.getInt("te_id"), results.getString("te_name"));
                TeamList_1.add(team_2);
                jComboBox3.addItem(results.getString("te_name"));

                if (results.getInt("te_id") == oldTeam1Id) {
                    jComboBox3.setSelectedItem(results.getString("te_name"));
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //to show data in team2 combo box
    private void Team_2CBFillData() {
        try {
            System.out.println("--------------6");
            Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            Statement mystmt = mycon.createStatement();
            String selectQuery = "select * from Team";
            ResultSet results = mystmt.executeQuery(selectQuery);
            while (results.next()) {
                Team team_2 = new Team(results.getInt("te_id"), results.getString("te_name"));
                TeamList_2.add(team_2);
                jComboBox4.addItem(results.getString("te_name"));
                if (results.getInt("te_id") == oldTeam2Id) {
                    jComboBox4.setSelectedItem(results.getString("te_name"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //to show data in venue combo box
    private void VenueCBFillData() {
        try {
            System.out.println("--------------7");
            Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            Statement mystmt = mycon.createStatement();
            String selectQuery = "select * from Venue";
            ResultSet results = mystmt.executeQuery(selectQuery);
            while (results.next()) {
                Venue veu = new Venue(results.getInt("v_id"), results.getString("v_name"));
                VenueList.add(veu);
                jComboBox5.addItem(results.getString("V_name"));
                if (results.getInt("v_id") == oldVenueId) {
                    jComboBox5.setSelectedItem(results.getString("v_name"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 0, 102));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Edit Matches Details");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Select Tournament");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Match Name");

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Select Team1");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Select Team2");

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Select new Venue");

        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(27, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addGap(63, 63, 63))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(52, 52, 52))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(31, 31, 31)))))
                .addGap(0, 35, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MatchesInfo a1 = new MatchesInfo();
        a1.show();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        /// get venue id
        String venueName = jComboBox5.getSelectedItem().toString();
        int venueId = VenueList.get(0).id;
        for (int i = 0; i < VenueList.size(); i++) {
            Venue t = VenueList.get(i);
            if (t.name.equals(venueName)) {
                venueId = t.id;
            }
        }
        //get matchId
        String matchName = jComboBox2.getSelectedItem().toString();
        int matchId = MatchList.get(0).id;
        for (int i = 0; i < MatchList.size(); i++) {
            Match t = MatchList.get(i);
            if (t.name.equals(matchName)) {
                matchId = t.id;
            }
        }
        //get turnment id
        String tou_name = jComboBox6.getSelectedItem().toString();
        int touId = TournamentList.get(0).id;
        for (int i = 0; i < TournamentList.size(); i++) {
            Tournament t = TournamentList.get(i);
            if (t.name.equals(tou_name)) {
                touId = t.id;
            }
        }
        
                    
        //update team1 id in team_mat table
            String team1Name = jComboBox3.getSelectedItem().toString();
            int newTeam1Id = TeamList_1.get(0).id;
            for (int i = 0; i < TeamList_1.size(); i++) {
                Team t = TeamList_1.get(i);
                if (t.name.equals(team1Name)) {
                    newTeam1Id = t.id;
                }
            }
            
            //update team2 id in team_mat table
            String team2Name = jComboBox4.getSelectedItem().toString();
            int newTeam2Id = TeamList_2.get(0).id;
            for (int i = 0; i < TeamList_2.size(); i++) {
                Team t = TeamList_2.get(i);
                if (t.name.equals(team2Name)) {
                    newTeam2Id = t.id;
                }
            }
            
          //same team should not be added
        if(team1Name.equals(team2Name )){
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f,"Same team selected.");
            return;
        }
        

        try {

                   
            //update venue id in matches table
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
            PreparedStatement stmt = con.prepareStatement("update matches m\n"
                    + "set m.venue_v_id = ?\n"
                    + "where m.m_id = ? and m.tournament_tou_id = ?");
            stmt.setInt(1, venueId);
            stmt.setInt(2, matchId);
            stmt.setInt(3, touId);
            int i = stmt.executeUpdate();
            System.out.println(i + "matches record updated");

            
            
            
            
            stmt = con.prepareStatement("update team_mat tm\n"
                    + "set tm.team_te_id =?\n"
                    + "where tm.team_te_id = ? and tm.matches_m_id = ?");
            stmt.setInt(1, newTeam1Id);
            stmt.setInt(2, oldTeam1Id);
            stmt.setInt(3, matchId);
            System.out.println("new: "+newTeam1Id+" old: "+oldTeam1Id+" match: "+matchId);
            i = stmt.executeUpdate();
            System.out.println(i + "team1 record updated");


            stmt = con.prepareStatement("update team_mat tm\n"
                    + "set tm.team_te_id =?\n"
                    + "where tm.team_te_id = ? and tm.matches_m_id = ?");
            stmt.setInt(1, newTeam2Id);
            stmt.setInt(2, oldTeam2Id);
            stmt.setInt(3, matchId);
            System.out.println("new: "+newTeam2Id+" old: "+oldTeam2Id+" match: "+matchId);
            i = stmt.executeUpdate();
            System.out.println(i + "team2 record updated");
            con.close();
            if (i > 0) {
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, "Matches updated sucessfully.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:

        if (jComboBox2.getSelectedItem() != null) {
            //if match changes from drop down, change team1, team2, and venue based on match id
            String matchName = jComboBox2.getSelectedItem().toString();

            int matchId = MatchList.get(0).id;
            for (int i = 0; i < MatchList.size(); i++) {
                Match t = MatchList.get(i);
                if (t.name.equals(matchName)) {
                    matchId = t.id;
                }
            }
            //get team1 and team2 based on matchId
            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
                PreparedStatement stmt = con.prepareStatement("select t.te_name\n"
                        + "from team t\n"
                        + "inner join team_mat tm on tm.team_te_id = t.te_id\n"
                        + "where tm.matches_m_id = ?");
                stmt.setInt(1, matchId);
                ResultSet rst = stmt.executeQuery();

                int i = 0;
                while (rst.next()) {
                    if (i == 0) {
                        jComboBox3.setSelectedItem(rst.getString("te_name"));
                        i++;
                    } else {
                        jComboBox4.setSelectedItem(rst.getString("te_name"));
                    }
                }
                //getvenue based on matchId
                stmt = con.prepareStatement("select v.v_name \n"
                        + "from venue v\n"
                        + "inner join matches m on m.venue_v_id = v.v_id\n"
                        + "where m.m_id = ?");
                stmt.setInt(1, matchId);
                rst = stmt.executeQuery();

                while (rst.next()) {
                    //oldVenueId = rst.getInt("v_id");
                    jComboBox5.setSelectedItem(rst.getString("v_name"));
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:

        if (evt.getModifiers() != 0) {

            System.out.println("hello");
            String tou_name = jComboBox6.getSelectedItem().toString();
            //find tournment id
            int touId = TournamentList.get(0).id;
            for (int i = 0; i < TournamentList.size(); i++) {
                Tournament t = TournamentList.get(i);
                if (t.name.equals(tou_name)) {
                    touId = t.id;
                }
            }
            jComboBox2.removeAllItems();
            MatchList = new ArrayList<>();
            //update matches
            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
                PreparedStatement stmt = con.prepareStatement("select m_id, m_name from MATCHES where tournament_tou_id =? ");
                stmt.setInt(1, touId);
                ResultSet rst = stmt.executeQuery();
                while (rst.next()) {
                    Match m = new Match(rst.getInt("m_id"), rst.getString("m_name"));
                    MatchList.add(m);
                    jComboBox2.addItem(rst.getString("m_name"));
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            //update team1, team2, and venue based on match id
            String matchName = jComboBox2.getSelectedItem().toString();
            int matchId = MatchList.get(0).id;
            for (int i = 0; i < MatchList.size(); i++) {
                Match t = MatchList.get(i);
                if (t.name.equals(matchName)) {
                    matchId = t.id;
                }
            }
            //get team1 and team2 based on matchId
            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521 ", "HR", "hr");
                PreparedStatement stmt = con.prepareStatement("select t.te_name\n"
                        + "from team t\n"
                        + "inner join team_mat tm on tm.team_te_id = t.te_id\n"
                        + "where tm.matches_m_id = ?");
                stmt.setInt(1, matchId);
                ResultSet rst = stmt.executeQuery();

                int i = 0;
                while (rst.next()) {
                    if (i == 0) {
                        jComboBox3.setSelectedItem(rst.getString("te_name"));
                        i++;
                    } else {
                        jComboBox4.setSelectedItem(rst.getString("te_name"));
                    }
                }
                //getvenue based on matchId
                stmt = con.prepareStatement("select v.v_name \n"
                        + "from venue v\n"
                        + "inner join matches m on m.venue_v_id = v.v_id\n"
                        + "where m.m_id = ?");
                stmt.setInt(1, matchId);
                rst = stmt.executeQuery();

                while (rst.next()) {
                    //oldVenueId = rst.getInt("v_id");
                    jComboBox5.setSelectedItem(rst.getString("v_name"));
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }//GEN-LAST:event_jComboBox6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditMatches.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditMatches.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditMatches.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditMatches.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditMatches().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
