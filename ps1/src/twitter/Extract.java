/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	assert (tweets.size() > 0);
    	
    	Instant start = tweets.get(0).getTimestamp();
    	Instant end = tweets.get(0).getTimestamp();
    	
    	for (Tweet tweet : tweets) {
    		if (start.compareTo(tweet.getTimestamp()) > 0) {
    			start = tweet.getTimestamp();
    		}
    		if (end.compareTo(tweet.getTimestamp()) < 0) {
    			end = tweet.getTimestamp();
    		}
    	}
    	
    	return new Timespan(start, end);
    }

    /**
     * Get the lowercase of usernames metioned in the given text
     * @param text the text of a tweet.
     * @return the list of lowercase of usernames metioned in the given text.
     */
    public static ArrayList<String> getMentionedUsernames(String text) {
        ArrayList<String> mentionedUsers = new ArrayList<>();

        // Regular expression to match valid Twitter usernames
        String regex = "(?<=^|[^\\w@._-])@([\\w]+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String username = matcher.group(1).toLowerCase();
            mentionedUsers.add(username);
        }

        return mentionedUsers;
    }
    
    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	ArrayList<ArrayList<String>> allMentionedUsersArrayLists = new ArrayList<ArrayList<String>>();
        for (Tweet tweet : tweets) {
        	allMentionedUsersArrayLists.add(getMentionedUsernames(tweet.getText()));
        }
        Set<String> set = allMentionedUsersArrayLists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
        return set;
    }
    



}
