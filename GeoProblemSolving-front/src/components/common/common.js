const raFrame =
  window.requestAnimationFrame ||
  window.webkitRequestAnimationFrame ||
  function(callback) {
    return window.setTimeout(callback, 1000 / 60);
  };
  
export function throttle(fn, context = this, isImmediate = false) {
  let isLocked;
  return function() {
    const _args = arguments;

    if (isLocked) return;

    isLocked = true;
    raFrame(function() {
      isLocked = false;
      fn.apply(context, _args);
    });

    isImmediate && fn.apply(context, _args);
  };
}