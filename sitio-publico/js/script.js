/* ==========================================================
   Club Los Algarrobos - script.js
   Un único archivo para las tres páginas. Cada bloque detecta
   si los elementos que necesita existen en la página actual.
   ========================================================== */

/* ----------------------------------------------------------
   1. Clase Actividad y datos (sección 3.3 del enunciado)
   ---------------------------------------------------------- */
class Actividad {
  constructor(id, nombre, disciplina, dias, horario, edadMinima, edadMaxima, cupoMaximo, cuotaMensual, profesor) {
    this.id = id;
    this.nombre = nombre;
    this.disciplina = disciplina;
    this.dias = dias;
    this.horario = horario;
    this.edadMinima = edadMinima;
    this.edadMaxima = edadMaxima;
    this.cupoMaximo = cupoMaximo;
    this.cuotaMensual = cuotaMensual;
    this.profesor = profesor;
  }

  // Método de negocio: ¿la actividad admite a alguien de esta edad?
  esAptaParaEdad(edad) {
    return edad >= this.edadMinima && edad <= this.edadMaxima;
  }

  // Formatea la cuota como moneda argentina
  cuotaFormateada() {
    return this.cuotaMensual.toLocaleString("es-AR", {
      style: "currency",
      currency: "ARS",
      maximumFractionDigits: 0
    });
  }

  rangoEdades() {
    return `${this.edadMinima} a ${this.edadMaxima} años`;
  }
}

const actividades = [
  new Actividad(1, "Fútbol infantil", "Fútbol", "Lun y Mié", "18:00", 6, 12, 40, 18000, "Diego Ferreyra"),
  new Actividad(2, "Vóley mixto", "Vóley", "Mar y Jue", "19:00", 13, 60, 24, 20000, "Laura Ceballos"),
  new Actividad(3, "Básquet juvenil", "Básquet", "Lun y Vie", "19:30", 12, 17, 20, 19000, "Martín Oviedo"),
  new Actividad(4, "Natación", "Natación", "Mar y Jue", "17:00", 5, 70, 30, 26000, "Sofía Bustos"),
  new Actividad(5, "Patín artístico", "Patín", "Mié y Vie", "17:30", 5, 15, 25, 21000, "Carla Peralta"),
  new Actividad(6, "Ajedrez", "Ajedrez", "Sáb", "10:00", 8, 99, 16, 9000, "Raúl Giménez"),
  new Actividad(7, "Funcional adultos", "Fitness", "Lun, Mié y Vie", "20:30", 18, 65, 30, 22000, "Paula Luna"),
  new Actividad(8, "Yoga", "Bienestar", "Mar y Jue", "9:00", 16, 99, 15, 17000, "Inés Arguello")
];

/* ----------------------------------------------------------
   2. Funciones de validación (RN7 y otras)
   ---------------------------------------------------------- */

// RN7: 7 u 8 dígitos numéricos, sin puntos
function esDniValido(dni) {
  return /^\d{7,8}$/.test(dni);
}

function esEmailValido(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email);
}

function calcularEdad(fechaNacimientoIso) {
  const nacimiento = new Date(fechaNacimientoIso);
  const hoy = new Date();
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const cumplioEsteAnio =
    hoy.getMonth() > nacimiento.getMonth() ||
    (hoy.getMonth() === nacimiento.getMonth() && hoy.getDate() >= nacimiento.getDate());
  if (!cumplioEsteAnio) {
    edad--;
  }
  return edad;
}

/* ----------------------------------------------------------
   3. Catálogo de actividades (actividades.html)
   ---------------------------------------------------------- */
function crearTarjeta(actividad) {
  const tarjeta = document.createElement("article");
  tarjeta.className = "tarjeta";
  tarjeta.dataset.id = actividad.id;

  tarjeta.innerHTML = `
    <span class="tarjeta__disciplina">${actividad.disciplina}</span>
    <h3>${actividad.nombre}</h3>
    <ul>
      <li><strong>Días:</strong> ${actividad.dias} - ${actividad.horario} hs</li>
      <li><strong>Edades:</strong> ${actividad.rangoEdades()}</li>
      <li><strong>Profesor/a:</strong> ${actividad.profesor}</li>
      <li><strong>Cupo:</strong> ${actividad.cupoMaximo} lugares</li>
    </ul>
    <p class="tarjeta__cuota">${actividad.cuotaFormateada()} / mes</p>
    <a href="preinscripcion.html?actividad=${actividad.id}" class="boton boton--primario">Preinscribirme</a>
  `;
  return tarjeta;
}

function renderizarCatalogo(lista) {
  const contenedor = document.getElementById("catalogo");
  const resultado = document.getElementById("resultado-filtro");

  // Vaciamos el contenedor y volvemos a dibujar
  contenedor.innerHTML = "";

  if (lista.length === 0) {
    const vacio = document.createElement("p");
    vacio.className = "sin-resultados";
    vacio.textContent = "No hay actividades que coincidan con el filtro.";
    contenedor.appendChild(vacio);
  } else {
    lista.forEach((actividad) => contenedor.appendChild(crearTarjeta(actividad)));
  }

  resultado.textContent = `Mostrando ${lista.length} de ${actividades.length} actividades`;
}

function aplicarFiltros() {
  const disciplina = document.getElementById("filtro-disciplina").value;
  const edadTexto = document.getElementById("filtro-edad").value;
  const edad = edadTexto === "" ? null : Number(edadTexto);

  const filtradas = actividades.filter((actividad) => {
    const cumpleDisciplina = disciplina === "" || actividad.disciplina === disciplina;
    const cumpleEdad = edad === null || actividad.esAptaParaEdad(edad);
    return cumpleDisciplina && cumpleEdad;
  });

  renderizarCatalogo(filtradas);
}

function cargarOpcionesDisciplina(select) {
  // Set elimina las disciplinas repetidas
  const disciplinas = [...new Set(actividades.map((a) => a.disciplina))].sort();
  disciplinas.forEach((disciplina) => {
    const opcion = document.createElement("option");
    opcion.value = disciplina;
    opcion.textContent = disciplina;
    select.appendChild(opcion);
  });
}

function iniciarCatalogo() {
  const selectDisciplina = document.getElementById("filtro-disciplina");
  const inputEdad = document.getElementById("filtro-edad");
  const btnLimpiar = document.getElementById("btn-limpiar");

  cargarOpcionesDisciplina(selectDisciplina);

  // Eventos registrados con addEventListener
  selectDisciplina.addEventListener("change", aplicarFiltros);
  inputEdad.addEventListener("input", aplicarFiltros);
  btnLimpiar.addEventListener("click", () => {
    selectDisciplina.value = "";
    inputEdad.value = "";
    aplicarFiltros();
  });

  renderizarCatalogo(actividades);
}

/* ----------------------------------------------------------
   4. Formulario de preinscripción (preinscripcion.html)
   ---------------------------------------------------------- */
function mostrarError(idCampo, mensaje) {
  const input = document.getElementById(idCampo);
  const spanError = document.getElementById(`error-${idCampo}`);
  spanError.textContent = mensaje;
  input.closest(".campo").classList.add("campo--error");
}

function limpiarError(idCampo) {
  const input = document.getElementById(idCampo);
  const spanError = document.getElementById(`error-${idCampo}`);
  spanError.textContent = "";
  input.closest(".campo").classList.remove("campo--error");
}

// Devuelve true si el formulario es válido; muestra errores junto a cada campo
function validarFormulario() {
  let valido = true;

  const nombre = document.getElementById("nombre").value.trim();
  const apellido = document.getElementById("apellido").value.trim();
  const dni = document.getElementById("dni").value.trim();
  const fechaNacimiento = document.getElementById("fechaNacimiento").value;
  const email = document.getElementById("email").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const actividadId = document.getElementById("actividad").value;
  const acepta = document.getElementById("acepta").checked;

  ["nombre", "apellido", "dni", "fechaNacimiento", "email", "telefono", "actividad", "acepta"].forEach(limpiarError);

  if (nombre === "") {
    mostrarError("nombre", "El nombre es obligatorio");
    valido = false;
  }

  if (apellido === "") {
    mostrarError("apellido", "El apellido es obligatorio");
    valido = false;
  }

  if (dni === "") {
    mostrarError("dni", "El DNI es obligatorio");
    valido = false;
  } else if (!esDniValido(dni)) {
    mostrarError("dni", "El DNI debe tener 7 u 8 dígitos numéricos, sin puntos");
    valido = false;
  }

  let edad = null;
  if (fechaNacimiento === "") {
    mostrarError("fechaNacimiento", "La fecha de nacimiento es obligatoria");
    valido = false;
  } else {
    edad = calcularEdad(fechaNacimiento);
    if (edad < 0 || edad > 110) {
      mostrarError("fechaNacimiento", "La fecha de nacimiento no es válida");
      valido = false;
    }
  }

  if (email === "") {
    mostrarError("email", "El email es obligatorio");
    valido = false;
  } else if (!esEmailValido(email)) {
    mostrarError("email", "Ingresá un email válido (ej: nombre@correo.com)");
    valido = false;
  }

  if (telefono !== "" && !/^[\d\s\-()+]{6,20}$/.test(telefono)) {
    mostrarError("telefono", "El teléfono solo puede tener números, espacios y guiones");
    valido = false;
  }

  if (actividadId === "") {
    mostrarError("actividad", "Elegí una actividad");
    valido = false;
  } else if (edad !== null) {
    // Validación cruzada: la edad debe estar dentro del rango de la actividad elegida
    const actividad = actividades.find((a) => a.id === Number(actividadId));
    if (actividad && !actividad.esAptaParaEdad(edad)) {
      mostrarError("actividad", `${actividad.nombre} es para ${actividad.rangoEdades()} (tu edad: ${edad})`);
      valido = false;
    }
  }

  if (!acepta) {
    mostrarError("acepta", "Tenés que aceptar que te contactemos");
    valido = false;
  }

  return valido;
}

function iniciarFormulario() {
  const form = document.getElementById("form-preinscripcion");
  const selectActividad = document.getElementById("actividad");
  const btnVolver = document.getElementById("btn-volver");

  // Cargar opciones del select a partir del arreglo
  actividades.forEach((actividad) => {
    const opcion = document.createElement("option");
    opcion.value = actividad.id;
    opcion.textContent = `${actividad.nombre} (${actividad.rangoEdades()})`;
    selectActividad.appendChild(opcion);
  });

  // Objeto del navegador: location. Si llegamos desde una tarjeta
  // (preinscripcion.html?actividad=3), preseleccionamos la actividad.
  const parametros = new URLSearchParams(window.location.search);
  const actividadPreseleccionada = parametros.get("actividad");
  if (actividadPreseleccionada) {
    selectActividad.value = actividadPreseleccionada;
  }

  // Objeto del navegador: history. El botón "Volver" vuelve a la página anterior.
  btnVolver.addEventListener("click", () => {
    if (window.history.length > 1) {
      window.history.back();
    } else {
      window.location.href = "index.html";
    }
  });

  // Validación en vivo: al salir de un campo con error, lo re-validamos
  form.querySelectorAll("input, select").forEach((campo) => {
    campo.addEventListener("blur", () => {
      if (campo.closest(".campo").classList.contains("campo--error")) {
        validarFormulario();
      }
    });
  });

  form.addEventListener("submit", (evento) => {
    // Evitamos el envío real: no hay backend para este formulario todavía
    evento.preventDefault();

    if (!validarFormulario()) {
      // Llevamos el foco al primer campo con error
      const primerError = form.querySelector(".campo--error input, .campo--error select");
      if (primerError) {
        primerError.focus();
      }
      return;
    }

    const nombre = document.getElementById("nombre").value.trim();
    const actividad = actividades.find((a) => a.id === Number(selectActividad.value));

    document.getElementById("texto-exito").textContent =
      `Gracias, ${nombre}. Registramos tu interés en ${actividad.nombre}. ` +
      `La secretaría se va a comunicar al email que dejaste para confirmar el cupo.`;

    form.classList.add("oculto");
    document.getElementById("mensaje-exito").classList.remove("oculto");
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}

/* ----------------------------------------------------------
   5. Elementos comunes a todas las páginas (pie)
   ---------------------------------------------------------- */
function iniciarPie() {
  const anio = document.getElementById("anio-actual");
  if (anio) {
    anio.textContent = new Date().getFullYear();
  }

  // Objetos del navegador: navigator y screen
  const info = document.getElementById("info-navegador");
  if (info) {
    const idioma = navigator.language || "es";
    info.textContent = `Idioma del navegador: ${idioma} · Pantalla: ${screen.width}x${screen.height}`;
  }
}

/* ----------------------------------------------------------
   6. Punto de entrada: esperamos a que el DOM esté listo
   ---------------------------------------------------------- */
document.addEventListener("DOMContentLoaded", () => {
  iniciarPie();

  if (document.getElementById("catalogo")) {
    iniciarCatalogo();
  }

  if (document.getElementById("form-preinscripcion")) {
    iniciarFormulario();
  }
});
