<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script type='text/javascript' data-cfasync='false'>window.purechatApi = {
  l: [], t: [], on: function () {
    this.l.push(arguments);
  }
};
(function () {
  var done = false;
  var script = document.createElement('script');
  script.async = true;
  script.type = 'text/javascript';
  script.src = 'https://app.purechat.com/VisitorWidget/WidgetScript';
  document.getElementsByTagName('HEAD').item(0).appendChild(script);
  script.onreadystatechange = script.onload = function (e) {
    if (!done && (!this.readyState || this.readyState == 'loaded' || this.readyState == 'complete')) {
      var w = new PCWidget({c: 'd4f3221c-42dc-41cb-8763-a61637b0cc4a', f: true});
      done = true;
    }
  };
})();</script>

<style>
body {
  margin: 0;
  padding: 0;
  cursor: url(assets/cursor/Arrow.cur), default;
}

button, a {
  cursor: url(assets/cursor/Hand.cur), pointer;
}

input {
  cursor: url(assets/cursor/Handwriting.cur), text;
}

textarea, input:focus {
  cursor: url(assets/cursor/IBeam.cur), text;
}

</style>
