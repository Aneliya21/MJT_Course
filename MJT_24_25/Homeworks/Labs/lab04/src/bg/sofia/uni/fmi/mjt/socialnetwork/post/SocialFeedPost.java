package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDateTime;

public class SocialFeedPost implements Post {

    private final String uniqueId;
    private final UserProfile author;
    private final LocalDateTime publishedOn;
    private final String content;
    private final Map<ReactionType, Set<UserProfile>> reactions;

    public SocialFeedPost(UserProfile author, String content) {

        this.uniqueId = UUID.randomUUID().toString();
        this.author = author;
        this.publishedOn = LocalDateTime.now();
        this.content = content;
        this.reactions = new HashMap<>();

        for (ReactionType type : ReactionType.values()) {
            reactions.put(type, new HashSet<>());
        }

    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException("User and Reaction cannot be null!");
        }
        if (reactions.get(reactionType).contains(userProfile)) {
            return false;
        }
        removeReaction(userProfile);
        reactions.get(reactionType).add(userProfile);
        return true;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        boolean removed = false;

        for (Set<UserProfile> users : reactions.values()) {
            if (users.remove(userProfile)) {
                removed = true;
                reactions.remove(userProfile);
            }
        }

        return removed;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {

        Map<ReactionType, Set<UserProfile>> unmodifiableReactions =
            new EnumMap<>(ReactionType.class);

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            unmodifiableReactions.put(entry.getKey(),
                Collections.unmodifiableSet(entry.getValue()));
        }

        return Collections.unmodifiableMap(unmodifiableReactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction cannot be null!");
        }
        return reactions.get(reactionType).size();
    }

    @Override
    public int totalReactionsCount() {

        int totalCount = 0;

        for (Set<UserProfile> users : reactions.values()) {
            totalCount += users.size();
        }

        return totalCount;
    }

}
