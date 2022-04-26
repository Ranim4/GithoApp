package training.GithooApp;

import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import training.GithoApp.Utilities.Alert;
import training.GithoApp.Utilities.Api;
import training.GithoApp.Utilities.InterventionImage;
import training.GithoApp.models.GithubUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainFrame extends JFrame {
    private static final String GENERIC_ERROR_MESSAGE = "Oooops, an error occured please try again";
    private static final String INTERNET_CONNECTION_PROBLEM = "Check your internet connection";
    private static final Color GRAY_COLOR = Color.decode("#3D4852");
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color LIGHT_GRAY_COLOR = new Color(255, 255, 255, 128);
    private static final Font DEFAULT_FONT = new Font("San Francisco", Font.PLAIN, 24);

    private JLabel nameOnFrame;
    private JLabel bioOnFrame;
    private JLabel avatarLabel;
    private URL url;

    private JPanel otherInfoPanel;
    private JLabel followers;
    private JLabel following;
    private JLabel followersCount;
    private JLabel followingCount;

    private JLabel location;

    private GithubUser githubUser;

    public MainFrame(String title) {
        super (title);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(new Insets(25, 25, 25, 25)));
        contentPane.setBackground(GRAY_COLOR);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        nameOnFrame = new JLabel("--", SwingConstants.CENTER);
        nameOnFrame.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameOnFrame.setBorder(new EmptyBorder(new Insets(0, 0, 20, 0)));
        nameOnFrame.setForeground(WHITE_COLOR);
        nameOnFrame.setFont(DEFAULT_FONT);

        bioOnFrame = new JLabel("...", SwingConstants.CENTER);
        bioOnFrame.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioOnFrame.setForeground(LIGHT_GRAY_COLOR);
        bioOnFrame.setFont(DEFAULT_FONT.deriveFont(18f));

        avatarLabel = new JLabel("");
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setBorder(new EmptyBorder(new Insets(20, 0, 0, 0)));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            getAvatarImage(new URL("https://semainedelhistoire.com/wp-content/uploads/2021/04/avatar_placeholder.png"));
        } catch (MalformedURLException e) {
            Alert.displayError(MainFrame.this, "Error downloading avatar icon");
        }


        otherInfoPanel = new JPanel(new GridLayout(2, 2));
        otherInfoPanel.setBackground(GRAY_COLOR);
        followers = new JLabel("Abonnés", SwingConstants.CENTER);
        followers.setForeground(LIGHT_GRAY_COLOR);
        followers.setFont(DEFAULT_FONT.deriveFont(16f));
        following = new JLabel("Abonnements", SwingConstants.CENTER);
        following.setForeground(LIGHT_GRAY_COLOR);
        following.setFont(DEFAULT_FONT.deriveFont(16f));
        followersCount = new JLabel("---", SwingConstants.CENTER);
        followersCount.setForeground(WHITE_COLOR);
        followersCount.setFont(DEFAULT_FONT);
        followingCount = new JLabel("---", SwingConstants.CENTER);
        followingCount.setForeground(WHITE_COLOR);
        followingCount.setFont(DEFAULT_FONT);

        otherInfoPanel.add(followers);
        otherInfoPanel.add(following);
        otherInfoPanel.add(followersCount);
        otherInfoPanel.add(followingCount);

        location = new JLabel("--, --", SwingConstants.CENTER);
        location.setAlignmentX(Component.CENTER_ALIGNMENT);
        location.setFont(DEFAULT_FONT.deriveFont(13f));
        location.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        location.setForeground(WHITE_COLOR);
     //   location.setBorder(new LineBorder(Color.red));

        contentPane.add(nameOnFrame);
        contentPane.add(bioOnFrame);
        contentPane.add(avatarLabel);
        contentPane.add(otherInfoPanel);
        contentPane.add(location);

        setContentPane(contentPane);

        String username = "Ranim4";
        String username1 = "mercuryseries";
        String username2 = "seven-valley";
        String username3 = "AnnaAgeneau";

//        System.out.println("Before request ...");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Api.getGithubUserUrl(username)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Alert.displayError(MainFrame.this, INTERNET_CONNECTION_PROBLEM);
            }

            @Override
            public void onResponse(Call call, Response response){
                try (ResponseBody body = response.body()) {
                    if (response.isSuccessful()){
                    String jsonData = body.string();
                    githubUser = getGithubUserDetails(jsonData);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    EventQueue.invokeLater( () -> {
                        try {
                            updateData();
                        } catch (MalformedURLException e) {
                            Alert.displayError(MainFrame.this, "Error downloading avatar icon");
                        }
                    });
                    System.out.println(githubUser.getName() + ",\n" + githubUser.getBiography() +
                            ",\n" + githubUser.getFollowersCount() + ",\n" + githubUser.getFollowingCount()
                            + ",\n" + githubUser.getLocation() + ",\n" + githubUser.getAvatarUrl());
                    } else {
                    Alert.displayError(MainFrame.this, GENERIC_ERROR_MESSAGE);
                    }
                }
                catch (ParseException | IOException e) {
                        Alert.displayError(MainFrame.this, GENERIC_ERROR_MESSAGE);
                    }
            }

            private GithubUser getGithubUserDetails(String jsonData) throws ParseException {
                JSONObject access;
                GithubUser githubUser = new GithubUser();
                access = (JSONObject) JSONValue.parseWithException(jsonData);
                githubUser.setName((String) access.get("name"));
                githubUser.setBiography ((String) access.get("bio"));
                githubUser.setFollowersCount(Double.valueOf(access.get("followers") + ""));
                githubUser.setFollowingCount( Double.valueOf(access.get("following") +""));
                githubUser.setLocation ((String) access.get("location"));
                githubUser.setAvatarUrl((String) access.get("avatar_url"));
                return githubUser;
            }
        });
   //     System.out.println("After request ...");
    }

    private void getAvatarImage(URL url) {
        try {
            BufferedImage bufferedImage = ImageIO.read(url);
            bufferedImage = InterventionImage.makeRoundedCorner(bufferedImage, 600);
            ImageIcon icon = InterventionImage.scale(bufferedImage, 200, 200);
            avatarLabel.setText("");
            avatarLabel.setIcon(icon);
        } catch(IOException e) {
            Alert.displayError(MainFrame.this, "Impossible d'afficher l'avatar.");
        }
    }

    private void updateData() throws MalformedURLException {
        nameOnFrame.setText(githubUser.getName());
        bioOnFrame.setText(githubUser.getBiography());
        getAvatarImage(new URL(githubUser.getAvatarUrl()));
        followersCount.setText(githubUser.getFollowersCount() + "");
        followingCount.setText(githubUser.getFollowingCount() + "");
        location.setText(githubUser.getLocation());
    }
    /* public class GithubWorker extends SwingWorker<String, Void> {
        private String url;
        public GithubWorker(String url) {
            this.url = url;
        }
        @Override
        protected String doInBackground() throws Exception {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()){
                    return response.body().string();
                }
            } catch (IOException e) {
                System.err.println("Error: " + e);
            }
            return null;
        }
        @Override
        protected void done() {
            try {
                System.out.println(get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            super.done();
        }
    }*/

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(500, 500);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(500, 500);
    }
}
