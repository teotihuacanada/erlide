package org.erlide.ui.wizards;

import java.io.File;
import java.net.URI;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.erlide.engine.NewProjectData;

@SuppressWarnings("all")
public class ErlangNewProjectCreationPage extends WizardNewProjectCreationPage {
  private final NewProjectData info;
  
  public ErlangNewProjectCreationPage(final String name, final NewProjectData info) {
    super(name);
    this.info = info;
  }
  
  @Override
  public void setVisible(final boolean visible) {
    super.setVisible(visible);
    if (visible) {
      this.onEntry();
    } else {
      this.onExit();
    }
  }
  
  protected void onEntry() {
  }
  
  protected void onExit() {
    String _projectName = this.getProjectName();
    this.info.setName(_projectName);
    URI _locationURI = this.getLocationURI();
    String _path = _locationURI.getPath();
    Path _path_1 = new Path(_path);
    this.info.setLocation(_path_1);
    boolean _projectExists = this.projectExists();
    this.info.setExistingProject(_projectExists);
  }
  
  private boolean projectExists() {
    final IPath loc = this.info.getLocation();
    if (((loc == null) || this.info.getName().isEmpty())) {
      return false;
    }
    final File dir = loc.toFile();
    return dir.exists();
  }
}
