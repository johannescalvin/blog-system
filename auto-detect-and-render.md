# 图表自动渲染

## 插入Latex公式

本系统能够[katex.js](https://github.com/KaTeX/KaTeX)自动检测并渲染markdown文本中的latex公式。

你只需要在markdown中插入

<pre>
```latex
\sum _{i = 0}^{10} (i^{2/3})= 25
```
</pre>

以上代码会自动被渲染为
![latex.png](images/latex.png)

## 插入序列图

插入序列图有多种方式，第一种使用[js-sequence-diagrams](https://bramp.github.io/js-sequence-diagrams/)自动检测并渲染序列图

<pre>
```sequence
    A->B: request
    B->C: request
    C-->A: response
```
</pre>

或者

<pre>
```seq
    A->B: request
    B->C: request
    C-->A: response
```
</pre>

以上两种方式插入的代码片段均会被渲染为

![seq.png](images/seq.png)

### 使用Mermaid

另一种是使用[mermaid](https://mermaidjs.github.io)自动检测和渲染序列图。

在markdown中插入片段

<pre>
```mermaid
sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob->>John: Jolly good!
```
</pre>

以上代码将会被自动渲染为

![mermaid-seq.png](images/mermaid-seq.png)

具体语法可参见[官方文档](https://mermaidjs.github.io/#/sequenceDiagram?id=syntax)

### 使用PlantUML语法

可以使用`PlantUML`创建流程图，具体语法参见[文档](http://plantuml.com/zh/sequence-diagram)

<pre>
```plantuml
@startuml
actor Bob #red
' The only difference between actor
'and participant is the drawing
participant Alice
participant "I have a really\nlong name" as L #99FF99
/' You can also declare:
   participant L as "I have a really\nlong name"  #99FF99
  '/

Alice->Bob: Authentication Request
Bob->Alice: Authentication Response
Bob->L: Log transaction
@enduml
```
</pre>

将被自动识别并渲染为

![plantuml-sequence.png](images/plantuml-sequence.png)


## 插入甘特图

### 使用Mermaid

<pre>
```mermaid
gantt
dateFormat  YYYY-MM-DD
title ADD GANTT diagram to mermaid
excludes weekdays 2014-01-10

section A section
Completed task            :done,    des1, 2014-01-06,2014-01-08
Active task               :active,  des2, 2014-01-09, 3d
Future task               :         des3, after des2, 5d
Future task2               :         des4, after des3, 5d
```
</pre>

将自动识别，并渲染为

![mermaid-gantt.png](images/mermaid-gantt.png)

### 使用PalntUML

系统可以使用`PlantUML`来画甘特图，根据[教程](http://plantuml.com/zh/gantt-diagram)来看，`PlantUML`还是能够满足比较精细的甘特图要求的。

使用下面的代码片段

<pre>
```plantuml
@startgantt
project starts the 2018/04/09
saturday are closed
sunday are closed
2018/05/01 is closed
2018/04/17 to 2018/04/19 is closed
[Prototype design] lasts 14 days
[Test prototype] lasts 4 days
[Test prototype] starts at [Prototype design]'s end
[Prototype design] is colored in Fuchsia/FireBrick 
[Test prototype] is colored in GreenYellow/Green 
@endgantt
```
</pre>

将被自动检测到并被渲染为

![plantuml-gantt](images/plantuml-gantt.png)

## 插入流程图

使用[mermaid的flowchart语法](https://mermaidjs.github.io/#/flowchart)插入流程图。

举个例子，在markdown文件中插入片段

<pre>
```mermaid
graph TB
    c1-->a2
    subgraph one
    a1-->a2
    end
    subgraph two
    b1-->b2
    end
    subgraph three
    c1-->c2
    end
```
</pre>

会被自动检测被渲染为

![mermaid-flowchart.png](images/mermaid-flowchart.png)

## 插入活动图

`PlantUML`的活动图，个人感觉，在某些场景下，要比流程图更具表现力。具体语法参见[文档](http://plantuml.com/zh/activity-diagram-beta)

示例: 在markdown文件中插入下面的片段

<pre>
```plantuml
@startuml
start
if (condition A) then (yes)
  :Text 1;
elseif (condition B) then (yes)
  :Text 2;
  stop
elseif (condition C) then (yes)
  :Text 3;
elseif (condition D) then (yes)
  :Text 4;
else (nothing)
  :Text else;
endif
stop
@enduml
```
</pre>

将自动识别并渲染为

![plantuml-activity-diagram.png](images/plantuml-activity-diagram.png)

## 插入UML类图

### 使用Mermaid

系统使用mermaid自动检测并渲染UML类图，在markdown文件中插入符合[语法](https://mermaidjs.github.io/#/classDiagram)的片段

<pre>
```mermaid
classDiagram
    Animal <|-- Duck
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
        +String beakColor
        +swim()
        +quack()
    }
    class Fish{
        -int sizeInFeet
        -canEat()
    }
    class Zebra{
        +bool is_wild
        +run()
    }
```
</pre>

以上代码片段将会被自动识别并渲染为

![mermaid-uml.png](images/mermaid-uml.png)

### 使用PantUML

具体语法参见[文档](http://plantuml.com/zh/class-diagram)

<pre>
```plantuml
@startuml
class Car

Driver - Car : drives >
Car *- Wheel : have 4 >
Car -- Person : < owns

@enduml
```
</pre>

将自动被识别，并渲染为

![plantuml-class-uml.png](images/plantuml-class-uml.png)

## 插入状态图

### 使用Mermaid

在markdown文件中插入符合[状态图语法](https://mermaidjs.github.io/#/stateDiagram)的片段

<pre>
```mermaid
stateDiagram
    [*] --> First
    First --> Second
    First --> Third

    state First {
        [*] --> fir
        fir --> [*]
    }
    state Second {
        [*] --> sec
        sec --> [*]
    }
    state Third {
        [*] --> thi
        thi --> [*]
    }
```
</pre>

将会被自动识别并渲染为

![mermaid-state-diagram.png](images/mermaid-state-diagram.png)

### 使用PlantUML

可以使用`PlantUML`插入状态图，具体语法参见[文档](http://plantuml.com/zh/state-diagram)。

举个例子，下面的代码片段

<pre>
```plantuml
@startuml
skinparam backgroundColor LightYellow
skinparam state {
  StartColor MediumBlue
  EndColor Red
  BackgroundColor Peru
  BackgroundColor<<Warning>> Olive
  BorderColor Gray
  FontName Impact
}

[*] --> NotShooting

NotShooting --> [*]
@enduml
```state "Idle mode" as Idle <<Warning>>
  state "Configuring mode" as Configuring
  [*] --> Idle
  Idle --> Configuring : EvConfig
  Configuring --> Idle : EvConfig
}

NotShooting --> [*]
@enduml
```
</pre>

将自动识别并渲染为

![plantuml-state-diagram.png](images/plantuml-state-diagram.png)

## 插入饼图

在markdown文件中插入符合[语法](https://mermaidjs.github.io/#/pie?id=syntax)的代码片段

<pre>

```mermaid
pie
    "Dogs" : 386
    "Cats" : 85
    "Rats" : 15
```
</pre>

将自动识别被渲染为饼图
![mermaid-pie.png](images/mermaid-pie.png)

## 插入思维导图

### 使用百度脑图

百度脑图是个相当完善的思维导图生成工具，并且在线分享。为保持markdown文件的简洁，直接使用百度脑图，编辑，并在`我的文档->需要分享的脑图->选项->分享设置->复制链接`，然后在markdown文件中插入

```text
[思维导图](百度脑图的分享链接)
```

### 使用plantUML

<pre>
```plantuml
@startmindmap
* Debian
** Ubuntu
*** Linux Mint
*** Kubuntu
*** Lubuntu
*** KDE Neon
** LMDE
** SolydXK
** SteamOS
** Raspbian with a very long name
*** <s>Raspmbc</s> => OSMC
*** <s>Raspyfi</s> => Volumio
@endmindmap
```
</pre>

自动检测并渲染

![mindmap.png](images/plantuml-mindmap.png)

## 插入定时图

系统基于`PlantUML`实现了定时图功能，语法参见[文档](http://plantuml.com/zh/timing-diagram)。

示例，在markdown文件中插入下面的代码

<pre>
```plantuml
@startuml
robust "Web Browser" as WB
concise "Web User" as WU

WB is Initializing
WU is Absent

@WB
0 is idle
+200 is Processing
+100 is Waiting
WB@0 <-> @50 : {50 ms lag}

@WU
0 is Waiting
+500 is ok
@200 <-> @+150 : {150 ms}
@enduml
```
</pre>

将被自动识别并渲染为

![plantuml-timing-diagram.png](images/plantuml-timing-diagram.png)

## 插入组件图

基于`PlantUML`提供了组件图功能，具体语法参见[文档](http://plantuml.com/zh/component-diagram)。

示例: 在markdown文件中插入下面的代码片段

<pre>
```plantuml
@startuml

package "Some Group" {
  HTTP - [First Component]
  [Another Component]
}
 
node "Other Groups" {
  FTP - [Second Component]
  [First Component] --> FTP
} 

cloud {
  [Example 1]
}


database "MySql" {
  folder "This is my folder" {
	[Folder 3]
  }
  frame "Foo" {
	[Frame 4]
  }
}


[Another Component] --> [Example 1]
[Example 1] --> [Folder 3]
[Folder 3] --> [Frame 4]

@enduml
```
</pre>

将自动识别被渲染为

![plantuml-component.png](images/plantuml-component-diagram.png)
