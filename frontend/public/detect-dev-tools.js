// detect-dev-tools.js

(function () {
  function detectDevTool(allow) {
    if (isNaN(+allow)) allow = 100;
    // 웹에서 allow 의 값을 99999등 수정시 적용되지 않음
    var start = +new Date();
    debugger;
    var end = +new Date();
    if (isNaN(start) || isNaN(end) || end - start > allow) {
      // 개발자 도구가 open 된 것을 감지했을 때 실행할 코드 삽입
      alert("개발자 도구가 감지되었습니다!");
      document.location.href = "/404";
    }
  }

  if (window.attachEvent) {
    if (
      document.readyState === "complete" ||
      document.readyState === "interactive"
    ) {
      detectDevTool();
      window.attachEvent("onresize", detectDevTool);
      window.attachEvent("onmousemove", detectDevTool);
      window.attachEvent("onfocus", detectDevTool);
      window.attachEvent("onblur", detectDevTool);
    } else {
      setTimeout(arguments.callee, 0);
    }
  } else {
    window.addEventListener("load", detectDevTool);
    window.addEventListener("resize", detectDevTool);
    window.addEventListener("mousemove", detectDevTool);
    window.addEventListener("focus", detectDevTool);
    window.addEventListener("blur", detectDevTool);
  }
})();
