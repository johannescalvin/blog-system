function addTabs(num,toolbar,canvas) {
    var tabs_wrapper = document.createElement("div");
    tabs_wrapper.setAttribute("id","tabs-wrapper-"+ num);
    var tabs_with_img = document.createElement("div");
    tabs_with_img.setAttribute("title","展示图像");
    var tabs_with_code = document.createElement("div");
    tabs_with_code.setAttribute("title","展示代码");
    tabs_wrapper.appendChild(tabs_with_img);
    tabs_wrapper.appendChild(tabs_with_code);

    $(tabs_wrapper).insertBefore($(toolbar));

    tabs_with_code.appendChild(toolbar);
    tabs_with_img.appendChild(canvas);

    $("#"+"tabs-wrapper-"+num).tabs({
        border:false
    });
}