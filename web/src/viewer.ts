export interface Config {
  imgElementId: string;
  fpsEl: string;
  resEl: string;
  demoBase64?: string;
}

function el(id: string) {
  return document.getElementById(id) as HTMLElement | null;
}

export const EdgeViewer = {
  init(cfg: Config) {
    const img = document.getElementById(cfg.imgElementId) as HTMLImageElement;
    const fps = el(cfg.fpsEl);
    const res = el(cfg.resEl);

    if (cfg.demoBase64) {
      img.src = cfg.demoBase64;
      img.onload = () => {
        if (res) res.innerText = `Resolution: ${img.naturalWidth}x${img.naturalHeight}`;
      };
    }

    // fake FPS counter demo
    let frameCount = 0;
    setInterval(() => {
      frameCount++;
      if (fps) fps.innerText = `FPS: ${10 + Math.floor(Math.random() * 10)}`;
    }, 1000);
  }
};

// make global for HTML
(window as any).EdgeViewer = EdgeViewer;

