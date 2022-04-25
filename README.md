[<u>Members</u>](#members)

[<u>Description</u>](#description)

[<u>System Design and
Implementation</u>](#system-design-and-implementation)

> [<u>Start and Finish Rest</u>](#start-and-finish-rest)
>
> [<u>Start Focus Mode</u>](#start-focus-mode)
>
> [<u>Stay in Focus Mode</u>](#stay-in-focus-mode)
>
> [<u>Open Whitelisted Apps in Focus
> Mode</u>](#open-whitelisted-apps-in-focus-mode)
>
> [<u>Exit the Focus Mode</u>](#exit-the-focus-mode)

[<u>Concepts and their applications from Information Systems and
Programming</u>](#concepts-and-their-applications-from-information-systems-and-programming)

[<u>Contributions</u>](#section-1)

### **Members**

Lin Yutian 1004881

Liu Renhang 1004873

Meng Fanyi 1004889

Lee Pei Xuan 1005513

Sun Zhengnan 1004882

Lim Hong Jun Joshua 1005259

Lim Si Hui Brenda 1004578

### Description

The advent of digital devices has helped increase many students’
academic productivity, and also made various recreational apps
available, which may serve as distractions. Although taking short breaks
on such apps can be good for productivity, students are often tempted to
procrastinate on their work due to the addictive nature of these
recreational apps.

Hourglass is a focus app which aims to help individuals to manage their
time better by limiting the amount of time they spend on such apps.
Hourglass thus allows students to take short phone breaks without
worrying about not having the discipline to get back to work.

Hourglass helps the user time their rest and focus activities and
notifies the user once time is up. During the focus period, access to
apps, other than those whitelisted, are denied. In Settings, the user
can configure his default rest/focus time length, customize the
whitelist apps, and write motivational messages to be sent to themselves
at specified times. The app adopts a fresh and clear style, combining
visual comfort and ease of usage.

Further improvements include implementing a user database for account
systems, introducing a friend interaction feature, and a points and
achievement system.

### System Design and Implementation

#### Start and Finish Rest

Once the user enters our app, the user is in the **WaitRest state**. The
user can set a rest time by dragging the progress bar, and the user
enters **Rest state** by pressing the “START REST” button. Then
MainActivity enters **WaitFocus** mode in the following 2 situations:

1.  Rest time is up (the timer goes off).

2.  The user pressed “FINISH REST” to finish rest early.

If the user is playing other apps when the timer goes off, a
notification will be sent to remind the user to go back.

The diagram demonstrates how the **three states** interact with each
other.

<img src="media/image1.png" style="width:6.26772in;height:4.19444in" />

***Implementation Details:***

An abstract HomepageState class is used to implement the three modes:
WaitRest state, Rest state, WaitFocus state. This makes it easy to
switch between different states and allows for assets such as
timeViewModel, timeSegment and circularSeekBar to be easily reused for
both modes.

#### **Start Focus Mode**

After entering **WaitFocus** state, the user is ready to enter the focus
mode. The user can set how long he/she wants to be focused by dragging
the progress bar. The FocusActivity will started in the following 2
situations:

1.  The user presses the “START FOCUS” button.

2.  The time that the user has procrastinated in other apps reached the
    > snooze time the user set in the settings page (or 2 minutes by
    > default). In this case, the focus timer will be set to the default
    > focus time the user set in the settings page (or 25 minutes by
    > default).

The following diagram describes how the user enters focus mode.

<img src="media/image7.png" style="width:6.26772in;height:3.34722in" />

#### **Stay in Focus Mode**

Once the user enters the focus mode, FocusActivity is responsible for
locking the phone. The FocusLifecycleObserver class is used to observe
the change in the state of the lifecycle of the FocusActivity. When the
user tries to exit the Focus mode, the lifecycle will no longer be
Resumed. Depending on how the user attempts to exit, we either send an
immutable PendingIntent to the context or simply restart the
FocusActvitiy.

#### **Open Whitelisted Apps in Focus Mode**

WhitelistChooserActivity and WhitelistActivity are used in conjunction
to implement the whitelist function for Focus mode.
WhitelistChooserActivity scans the phone for installed apps to allow the
user to choose their whitelisted apps. The diagram below shows how the
user is able to choose the whitelisted apps.

<img src="media/image4.png" style="width:5.36499in;height:2.93203in" />

WhitelistActivity sets up the whitelist activity for users to choose
their whitelisted apps.

<img src="media/image5.png" style="width:5.61979in;height:3.60884in" />

The system architecture follows.

<img src="media/image3.png" style="width:6.26772in;height:3.13889in" />

The timer is implemented using the Timer and TimerViewModel class. The
former uses CountDownTimer to create the countdown sequence while the
latter is used to display the timer in the app.

#### **Exit the Focus Mode**

In our design, every user only has 3 chances to forcibly exit the focus
mode. Otherwise, the user has to wait until the timer goes off. To
forcibly exit, the ForceExitFragment makes use of the dialog to cover
the FocusActivity, and prompt the user with a confirmation message, if
the user still has enough chances to exit. And an easy math question
will pop-up when they confirm to exit. If the user answers correctly,
the FocusActivity exits. If not, the dialog closes and the user remains
in Focus mode. The math question design aims to give our users a time
buffer, so that they can think twice whether they really need to
forcibly exit the focus mode. The diagram below shows

<img src="media/image6.png" style="width:6.26772in;height:3.375in" />

The system architecture follows.

<img src="media/image2.png" style="width:6.26772in;height:3.63889in" />

###  

### Concepts and their applications from Information Systems and Programming

1.  Design Patterns

    1.  Singletons

    2.  Observers

    3.  States

2.  Fragments

3.  LifeCycle

4.  Explicit/Implicit intents

5.  OnActivityResult

6.  ListViewAdapter

### 

### Contributions

| Name                | Contributions        |
|---------------------|----------------------|
| Lin Yutian          | UI/UX Design, Report |
| Liu Renhang         | Backend Code, Report |
| Meng Fanyi          | Backend Code, Video  |
| Lee Pei Xuan        | UI/UX Design, Report |
| Sun Zhengnan        | UI/UX Design, Report |
| Lim Hong Jun Joshua | Backend Code, Report |
| Lim Si Hui Brenda   | UI/UX Design, Poster |
