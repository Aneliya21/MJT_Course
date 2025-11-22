package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {

    private final String username;
    private final Set<Interest> interests;
    private final Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        this.interests = new HashSet<>();
        this.friends = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {

        if(interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }

        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {

        if(interest == null) {
            throw new IllegalArgumentException("Interest cannot be null!");
        }

        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile friend) {

        if(friend == this) {
            throw new IllegalArgumentException("You cannot be a friend to yourself!");
        }

        if(friend == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        return friends.add(friend);
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {

        if(userProfile == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        return friends.remove(userProfile);
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {

        if(userProfile == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        return friends.contains(userProfile);
    }
}
