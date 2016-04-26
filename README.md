# Reward system

A company is planning a way to reward customers for inviting their friends. They're planning a reward system that will
give a customer points for each confirmed invitation they played a part into. The definition of a confirmed invitation is one where another invitation's invitee invited someone.

The inviter gets `(1/2)^k` points for each confirmed invitation, where k is the level of the invitation:
- level 0 (people directly invited) yields 1 point;
- level 1 (people invited by someone invited by the original customer) gives 1/2 points;
- level 2 invitations (people invited by someone on level 1) awards 1/4 points and so on.

Only the first invitation counts: multiple invites sent to the same person don't produce any further points, even if they come from different inviters.

Also, to count as a valid invitation, the invited customer must have invited someone (so customers that didn't invite anyone don't count as points for the customer that invited them).

So, given the input:
```
1 2
1 3
3 4
2 4
4 5
4 6
```

___

The score is:

* 1 - 2.5 (2 because he invited 2 and 3 plus 0.5 as 3 invited 4)
* 3 - 1 (1 as 3 invited 4 and 4 invited someone)
* 2 - 0 (even as 2 invited 4, it doesn't count as 4 was invited before by 3)
* 4 - 0 (invited 5 and 6, but 5 and 6 didn't invite anyone)
* 5 - 0 (no further invites)
* 6 - 0 (no further invites)

Note that 2 invited 4, but, since 3 invited 4 first, customer 3 gets the points.
___

## Running

To start a web server for the application, run:

    lein ring server

___

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen
