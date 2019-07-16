# ScienceVision

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)
## Overview
### Description
Android app that uses image recognition to provide children with education science material based on their scans.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Science Education
- **Mobile:** Super Mobile because it deals with the environment surrounding the user
- **Story:** Kid is bored, and bothering parent with lots of questions that the parent can't answer. Kid opens app and holds camera up to an object in their house. They learn how science is important to the object's purpose, and a fun hands-on experiment demonstrating scientific properties of the object. Kid is inspired to learn more about science!
- **Market:** Kids ages 7-12 (potentially also parents and teachers)
- **Habit:**  Social aspect, badges
- **Scope:** Computer vision, Databases, API to make text simpler and fit to read for children

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can add a scan an item and recieve inforamtion
* User can send a link to the experiment info
* User can view history of searches
* User can bookmark favorite experiments
* User can report potentially unsafe content

**Optional Nice-to-have Stories**
* Custom Camera
* Suggested items
* Users can share photos of their experiments
* Users can earn badges for completing experiments
* Users (science teachers?) can submit their own experiment ideas



### 2. Screen Archetypes

* Sign Up Page
    * User can sign up or log in
* Scanning 
    * Scans an item to identify what it is
* Item Detail View
    * report feedback form, add ideas from experiments and say if something isn't safe
* Profile
    * Badges, favorite experiments, all items scanned
* Social feed
    * A social feed that keeps the user coming back by showing some sort of readings



### 3. Navigation

**Tab Navigation** (Tab to Screen)
3 tabs:
* Scanning
* Profile
* Social


**Flow Navigation** (Screen to Screen)
* Sign up -> Profile (which has the 3 tabs)
* Scanning -> Item Detail
* Profile -> Item Detail
* Social -> Profile
* Social -> Item Detail

## Wireframes
Wireframes:
https://marvelapp.com/1gjjdh8g

## Schema 

### Models
<img src ='https://i.imgur.com/hc7xiOH.png\'/>
**Users**
* username (string)
* password (string)
* badges (enum?)
* findings

**Findings**
Items found
* image (Parse file)
* name (String)
* fun fact (String)
* experiments (URI link to experiment, could later become its own type)

### Networking
- 1. Signup/Login Activity
    - Create: Parse create a new user
    - Read: Parse logging in to user's profile
- 2. Scanning
    - Read: MLKit return item name
    - Read: Google search return fun fact, experiment, image?
    - Create: Parse create a new finding
- 3. Profile, Social
    - Read: Parse return findings
