## Project concept description
- Create a front-end view of my original Pokemon Battler Java project
  - Pokemon battling system.
  - User chooses 6 different Pokemon, computer choose 6 random different pokemon.
  - Players fight until a player has 0 useable Pokemon.
- Create user accounts and save game to user.
- Data saved to a Postgres database

## High-level plan/description for each milestone.
### Milestone 1: Initial Setup and Landing Page

Goal: Get you off the ground with a minimal running Spring project.

Subtasks
- Initialize your project with Spring Initializr.
- Add a @Controller with a route to handle /.
- Create a template for the landing page.
- Put it on GitHub.

### My Plan
By the end of this milestone, I expect to have a landing page that will contain options to either start a new game, or continue an old one if one exists. Past that, there will be a navbar with placeholder buttons to sign up, log in/log out, etc.
This shouldn't take more than a couple of days.

### Milestone 2: Content List Page and Detail Pages
Goal: Add a couple of extra routes that build dynamic pages based on data from a database.

Subtasks
- Create @Entitys to represent your domain data.
- Add database dependencies.
- Create and configure SQL migrations and initial data.
- Create CrudRepositorys to handle data access.
- Create @Controller to handle routes for list page and detail page.
- Create templates for list page and detail page.

### My Plan
So theoretically, I could complete this milestone by way of using the swap pokemon option as the list view, and also in that menu, allowing the user to select a pokemon and see specific details of it.
So I believe that's what I'm gonna go with. It's the only thing that really makes sense considering the scope of the game. This all should take a week max.

### Milestone 3: User Management
Goal: Add the ability for users to sign up, login, and logout.

Subtasks
- Create sign up page
- Create Service, Entity, and Repository for user accounts
- Configure Spring Security to properly handle password management
- Create login page
- Add logout

### My Plan
This one is pretty straight forward. I'm just going to set up a user system that stores user information inside a database. The sign up/sign in options will be in the header and they will be relatively simple pages. An interesting idea might be to set up some kind of password recovery/reset system.
This should take 2-4 days max.

### Milestone 4: Everything Else
Goal: Move game logic from original Java project to this and set up game page.

Subtasks
- Migrate original game logic from Java project
- Refactor to fit the needs of Spring project
- Create template from game page.

### My Plan
This part will probably take up the majority of my time. I'm going to have to move over the original game logic and refactor it to meet my needs for Spring. Then I want to make a template for the game page that hopefully implements some game sprites for each different pokemon. This game page would include basically everything (Setting up team on new game, fight options, swapping pokemon, viewing pokemon details, end screen, etc).
I believe that all of this work will take a minimum of a week to finish, if not more (probably more).
