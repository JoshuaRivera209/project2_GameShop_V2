package com.example.project2_gameshop_v2.userActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.adminActivities.ManageAppActivity;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;
import com.example.project2_gameshop_v2.startupActivities.LoginActivity;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project2_gameshop_v2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project2_gameshop_v2.PREFERENCES_KEY";
    private Button mAdminButton;
    private Button mBrowseButton;
    private Button mSearchButton;
    private Button mReturnButton;
    private Button mViewOwnedButton;

    private GameShopDAO mGameShopDAO;

    private int mUserId = -1;
    private User mUser;

    private SharedPreferences mPreferences = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getDatabase();
        getPrefs();
        wireupDisplay();
        checkForUser();
        checkforGames();
        loginUser(mUserId);
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    private void wireupDisplay() {
        mBrowseButton = findViewById(R.id.buttonBrowseGames);
        mBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = BrowseGamesActivity.intentFactory(getApplicationContext());
                startActivity(browseIntent);
            }
        });
        mSearchButton = findViewById(R.id.buttonSearchGames);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = SearchGamesActivity.intentFactory(getApplicationContext());
                startActivity(searchIntent);
            }
        });
        mViewOwnedButton = findViewById(R.id.buttonViewOwnedGames);
        mViewOwnedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewOwnedIntent = ViewOwnedActivity.intentFactory(getApplicationContext());
                startActivity(viewOwnedIntent);
            }
        });
        mReturnButton = findViewById(R.id.buttonReturnGames);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnGamesIntent = ReturnGamesActivity.intentFactory(getApplicationContext());
                startActivity(returnGamesIntent);
            }
        });
        mAdminButton = findViewById(R.id.buttonManageApp);
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminIntent = ManageAppActivity.intentFactory(getApplicationContext());
                startActivity(adminIntent);
            }
        });
    }

    private void checkForUser() {
        // do we have user in intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        if (mUserId != -1) {
            return;
        }

        if(mPreferences != null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1) {
            return;
        }

        // do we have any users at all?
        List<User> users = mGameShopDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            mGameShopDAO.insert(defaultUser);
            mGameShopDAO.update(defaultUser);
            User adminUser = new User("admin2", "admin2", true);
            mGameShopDAO.insert(adminUser);
            mGameShopDAO.update(adminUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    // Initializes all games on first-time login
    private void checkforGames() {
        List<Game> allGames = mGameShopDAO.getAllGames();
        if (allGames.size() <= 0) {
            // Rated E games
            Game nba2K = new Game("NBA 2K23", 59.99, "RATED E: Immerse yourself in the world of basketball, whether you like to create your own team, play with up-to-date rosters, get a taste of the past, or even play online with your own custom player, if you're a fan of basketball in any aspect then this game is for you.", 1);
            allGames.add(nba2K);
            Game nflFootball = new Game("Madden NFL", 59.99, "RATED E: Madden is the leading football video game franchise for a reason. From its realistic gameplay to the ability to customize virtually anything a football fan may ask for, Madden has been the ultimate all-in-one package for football fans for years on end.", 1);
            allGames.add(nflFootball);
            Game fifa = new Game("FIFA 23", 59.99, "RATED E: Fifa is back with another year of exciting content for soccer fans across the world. With the flagship game mode Ultimate Team, players can let their wildest dreams come true by being able to build any team that they want and have new, young players on the same team as the legends of the game. Purchase today to see the variety of other modes there are available!", 1);
            allGames.add(fifa);
            Game crashBandicoot = new Game("Crash Bandicoot N. Sane Trilogy", 29.99, "RATED E: Relive your childhood with a remake of this classic franchise! This bundle includes all 3 titles of the iconic Crash Bandicoot trilogy, reprised to meet the modern standards of gaming, but also kept as original as possible that way we can all feel the nostalgia of the games we know and love.", 1);
            allGames.add(crashBandicoot);
            Game zelda = new Game("Legend of Zelda: Tears of Kingdom (SWITCH ONLY)", 59.99, "RATED E: The highly anticipated sequel to Breath Of The Wild is finally here! Destined to be full of action and puzzles, follow link through yet another journey throughout Hyrule and defend the kingdom from an army of foes once more!", 1);
            allGames.add(zelda);

            // Rated T Games
            Game spiderman = new Game("Spiderman: Miles Morales", 44.99, "RATED T: Join the adventure of Miles Morales, as he follows the footsteps of his mentor Peter Parker in becoming the web-slinging Spiderman. In this sequel, Miles must take the reins and suit up to defend his city.", 1);
            allGames.add(spiderman);
            Game hogwarts = new Game("Hogwarts Legacy", 69.99, "RATED T: Easily one of the most anticipated titles of the year, Hogwarts Legacy is every Harry Potter fan's dream come true without a doubt. In this standalone title, explore all of Hogwarts and its surroundings in this free roam world as a student of the school. Get your own wand, cast the spells you want, and customize the look of your own character.", 1);
            allGames.add(hogwarts);
            Game horizon = new Game("Horizon Forbidden West", 59.99, "RATED T: A direct sequel to its previous title, Horizon Forbidden West is a continuation of Aloy's journey from the first installment. Explore the frontier, discover new machines to fight, and help Aloy bring balance to the world in this post-apocalyptic action and adventure title.", 1);
            allGames.add(horizon);
            Game streetFighter = new Game("Street Fighter 30th Anniversary (SWITCH ONLY)", 29.99, "RATED T: Relive your childhood with a remake of this classic franchise! Fight against a friend or the computer, enjoy this reprised classic on the Nintendo Switch.", 1);
            allGames.add(streetFighter);
            Game metroid = new Game("Metro Prime Remastered (SWITCH ONLY)", 39.99, "RATED T: Play behind the visor of the iconic franchise as Samus Aran. Relive the adventures of Samus as she navigates her way through a dangerous alien planet. This reprised first person adventure includes improved graphics, sounds, updated control schemes, and unlockable art.", 1);
            allGames.add(metroid);

            // Rated M Games
            Game RDR2 = new Game("Red Dead Redemption 2", 24.99, "RATED M: An iconic franchise, the second installment of Red Dead Redemption has accumulated over 175 game awards and has received over 250 perfect scores from critics. This treacherous tale of loyalty follows Arthur Morgan and the Van Der Linde gang, outlaws on the run, as they must rob, steal, and fight their way through the American lands in order to survive.", 1);
            allGames.add(RDR2);
            Game RE4 = new Game("Resident Evil 4", 59.99, "RATED M: In this remake of the classic series, join Leon S. Kennedy after the disaster of Racoon City, where his resolve led him to be recruited as an agent working with the President of the United States. In this remastered tale, Leon is tasked with saving the president's daughter after she is kidnapped, but as he sets out to complete his rescue mission he soon finds out the amount of horror that awaits him.", 1);
            allGames.add(RE4);
            Game deadSpace = new Game("Dead Space", 49.99,"RATED M: This classic sci-fi survival horror title returns completely rebuilt to improve the overall immersive experience from a horror aspect. With improved audio and graphics Isaac's journey to find his lover on his crew's massacred mining ship is more horrifying than ever." , 1);
            allGames.add(deadSpace);
            Game eldenRing = new Game("Elden Ring", 59.99, "RATED M: Winner of the Game of the Year Award, Elden Ring is a near perfect souls-like experience. For newcomers and hardcore fans of the genre, Elden Ring has difficult experiences for everyone. Explore this open-world action RPG title and become an Elden Lord as you rise and harness the power of the Elden Ring.", 1);
            allGames.add(eldenRing);
            Game deadIsland = new Game("Dead Island 2", 59.99, "RATED M: This long awaited sequel to Dead Island is finally here. With horror and dark humor mixed, this zombie slaying first-person adventure takes the player through a zombie-plagued Los Angeles. Realizing you are resistant to the virus, take on the undead as you uncover the truth about what you really are. Use everything you possibly can to craft your own weapons and turn LA into a bloody playground.", 1);
            allGames.add(deadIsland);

            for (Game g : allGames) {
                mGameShopDAO.insert(g);
            }
        }
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void loginUser(int userId) {
        mUser = mGameShopDAO.getUserById(userId);
        checkForAdmin(mUser);
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    private void checkForAdmin(User user) {
        if(user.isAdmin()) {
            mAdminButton.setVisibility(View.VISIBLE);
            return;
        }
        mAdminButton.setVisibility(View.INVISIBLE);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        Intent intent = LoginActivity.intentFactory(getApplicationContext());
                        startActivity(intent);
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we dont really need to do anything here.
                    }
                });
        alertBuilder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.landing_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null) {
            MenuItem item = menu.findItem(R.id.currentUser);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.landingMenuLogout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}