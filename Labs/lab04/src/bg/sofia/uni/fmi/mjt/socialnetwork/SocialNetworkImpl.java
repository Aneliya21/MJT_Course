package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Comparator;

public class SocialNetworkImpl implements SocialNetwork {

    private final Set<UserProfile> registeredUsers = new HashSet<>();
    private final List<Post> posts = new ArrayList<>();

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (!registeredUsers.add(userProfile)) {
            throw new UserRegistrationException("User is already registered!");
        }
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(registeredUsers);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (!registeredUsers.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered!");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty!");
        }

        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);

        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        Set<UserProfile> reachedUsers = new HashSet<>();
        UserProfile author = post.getAuthor();
        Set<Interest> authorInterests = new HashSet<>(author.getInterests());

        dfs(author, authorInterests, reachedUsers, new HashSet<>());

        reachedUsers.remove(author);
        return reachedUsers;
    }

    private void dfs(UserProfile current, Set<Interest> authorInterests,
                     Set<UserProfile> reachedUsers, Set<UserProfile> visited) {
        if (visited.contains(current)) {
            return;
        }

        visited.add(current);

        boolean hasCommonInterest = false;
        for (Interest interest : current.getInterests()) {
            if (authorInterests.contains(interest)) {
                hasCommonInterest = true;
                break;
            }
        }

        if (hasCommonInterest) {
            reachedUsers.add(current);
        }

        for (UserProfile friend : current.getFriends()) {
            if (!visited.contains(friend)) {
                dfs(friend, authorInterests, reachedUsers, visited);
            }
        }
    }

    private boolean isWithinNetwork(UserProfile origin, UserProfile target,
                                    Set<UserProfile> visited) {
        if (origin.isFriend(target)) {
            return true;
        }
        visited.add(origin);
        for (UserProfile friend : origin.getFriends()) {
            if (!visited.contains(friend)) {
                if (isWithinNetwork(friend, target, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
                                                throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("Both user profiles cannot be null!");
        }
        if (!registeredUsers.contains(userProfile1) || !registeredUsers.contains(userProfile2)) {
            throw new UserRegistrationException("One or both users are not registered");
        }

        Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());

        return mutualFriends;
    }

    private Comparator<UserProfile> friendsCountComparator() {
        return new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile u1, UserProfile u2) {
                int friendsCountComparison = Integer.compare(u2.getFriends().size(), u1.getFriends().size());

                if (friendsCountComparison != 0) {
                    return friendsCountComparison;
                }

                return u1.getUsername().compareTo(u2.getUsername());
            }
        };
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {

        SortedSet<UserProfile> sortedByFriends = new TreeSet<>(friendsCountComparator());
        sortedByFriends.addAll(registeredUsers);

        return sortedByFriends;
    }

}
