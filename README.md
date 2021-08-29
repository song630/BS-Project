# A vocabulary learning website 背单词网站
## Requirements: select a framework to design a website for learning vocabulary
## Basic features:
* User sign-up, login, information collection and verfication (e.g., username and password must have at leat 6 characters; email format verification; username and email must be unique in the system).
* After login, users can select the word books they want to study (e.g., TOEFL, GRE).
* Users can keep records of their favorite words.
* The website should supprot making study plan, reviewing, doing test and recording progress.
* The interface should fit the browsers from both PC and mobile phone.

## Augmented functions:
* Implement an Android or iPhone app, supporting off-line services, and could push notifications for the study plan.
* Has learning ability (adjust the study plan according to the user's pattern).

## Instructions:
---
1. sql scripts are put under `DB`.
2. The crawler is put under `WordLists\crawler`, which collects word books and generate `xml` files.
3. The complete back-end project is put under `web/Demo`, constructed using `SpringMVC`. Source code is put under `web\Demo\src\main\java\example`.
4. The complete front-end project is put under `web/front`, constructed using `Vue.js` + `ElementUI`. Source code is put under `src\components`.

## 使用说明
---
1. DB目录下是初始化table的sql脚本
2. WordLists\crawler下是java单词书爬虫工程(从有道爬取)，同时包括了生成单词书的xml文件的程序
3. web/Demo下是后端的完整工程，使用`SpringMVC`架构，源程序全部在web\Demo\src\main\java\example下
4. web/front下是前端的完整工程，使用`Vue.js` + `ElementUI`架构，源程序主要在src\components下
5. 开发过程前后端分离
